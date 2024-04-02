package vip.yeee.memo.common.scloud.gray.common.handle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Mono;
import vip.yeee.memo.common.scloud.gray.common.constant.CloudGrayConstant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/12/6 16:36
 */
@Slf4j
public class GrayLoadBalancer implements ReactorServiceInstanceLoadBalancer {

//    final AtomicInteger normalPosition;
//    final AtomicInteger grayPosition;

    final ConcurrentHashMap<String, AtomicInteger> position = new ConcurrentHashMap<>();

    final String serviceId;

    ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    public GrayLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }

    @Override
    // see original
    // https://github.com/Netflix/ocelli/blob/master/ocelli-core/
    // src/main/java/netflix/ocelli/loadbalancer/RoundRobinLoadBalancer.java
    public Mono<Response<ServiceInstance>> choose(Request request) {
        HttpHeaders headers = ((RequestDataContext) request.getContext()).getClientRequest().getHeaders();
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next()
                .map(serviceInstances -> processInstanceResponse(supplier, serviceInstances, headers));
    }

    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier,
                                                              List<ServiceInstance> serviceInstances, HttpHeaders headers) {
        String apiVersion = headers.getFirst(CloudGrayConstant.API_VERSION_HEADER);
        if (!StringUtils.hasText(apiVersion)) {
            javax.servlet.http.HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            apiVersion = request.getHeader(CloudGrayConstant.API_VERSION_HEADER);
        }
        Map<String,String> grayTagMap = new HashMap<>();
        grayTagMap.put(CloudGrayConstant.API_VERSION_HEADER, apiVersion);
        final Set<Map.Entry<String,String>> attributes = Collections.unmodifiableSet(grayTagMap.entrySet());
        Response<ServiceInstance> serviceInstanceResponse;
        log.info("【选择服务实例】- 服务version = {}", apiVersion);
        List<ServiceInstance> filteredInstances = Optional.ofNullable(serviceInstances).orElseGet(ArrayList::new)
                .stream()
                .filter(instance -> instance.getMetadata().entrySet().containsAll(attributes))
                .collect(Collectors.toList());
        serviceInstanceResponse = getInstanceResponse(apiVersion, filteredInstances);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }

    // RoundRobinLoadBalancer 轮询
    private Response<ServiceInstance> getInstanceResponse(String apiVersion, List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: " + serviceId + "--" + apiVersion);
            }
            return new EmptyResponse();
        }

        // Do not move position when there is only 1 instance, especially some suppliers
        // have already filtered instances
        if (instances.size() == 1) {
            return new DefaultResponse(instances.get(0));
        }

        // Ignore the sign bit, this allows pos to loop sequentially from 0 to
        // Integer.MAX_VALUE
        if (!position.containsKey(apiVersion) || position.get(apiVersion) == null) {
            position.put(apiVersion, new AtomicInteger(new Random().nextInt(1000)));
        }
        int pos = position.get(apiVersion).incrementAndGet() & Integer.MAX_VALUE;

        ServiceInstance instance = instances.get(pos % instances.size());

        return new DefaultResponse(instance);
    }

}
