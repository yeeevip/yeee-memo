//package vip.yeee.memo.integrate.common.scloud.gray.inner.handle;
//
//import com.google.common.collect.Lists;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.cloud.client.ServiceInstance;
//import org.springframework.cloud.client.loadbalancer.*;
//import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
//import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
//import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
//import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
//import org.springframework.http.HttpHeaders;
//import reactor.core.publisher.Mono;
//import vip.yeee.memo.integrate.base.model.constant.CloudGrayConstant;
//
//import java.util.*;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.stream.Collectors;
//
///**
// * description......
// *
// * @author yeeee
// * @since 2022/12/6 16:36
// */
//@Slf4j
//public class GrayLoadBalancer implements ReactorServiceInstanceLoadBalancer {
//
//    final AtomicInteger normalPosition;
//    final AtomicInteger grayPosition;
//
//    final String serviceId;
//
//    ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
//
//    public GrayLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
//        this(serviceInstanceListSupplierProvider, serviceId, new Random().nextInt(1000), new Random().nextInt(1000));
//    }
//
//    public GrayLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
//                                  String serviceId, int seedNormalPosition, int seedGrayPosition) {
//        this.serviceId = serviceId;
//        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
//        this.normalPosition = new AtomicInteger(seedNormalPosition);
//        this.grayPosition = new AtomicInteger(seedGrayPosition);
//    }
//
//    @Override
//    // see original
//    // https://github.com/Netflix/ocelli/blob/master/ocelli-core/
//    // src/main/java/netflix/ocelli/loadbalancer/RoundRobinLoadBalancer.java
//    public Mono<Response<ServiceInstance>> choose(Request request) {
//        HttpHeaders headers = ((RequestDataContext) request.getContext()).getClientRequest().getHeaders();
//        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
//                .getIfAvailable(NoopServiceInstanceListSupplier::new);
//        return supplier.get(request).next()
//                .map(serviceInstances -> processInstanceResponse(supplier, serviceInstances, headers));
//    }
//
//    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier,
//                                                              List<ServiceInstance> serviceInstances, HttpHeaders headers) {
//        String grayTag = headers.getFirst(CloudGrayConstant.GRAY_HEADER);
//        Map<String,String> grayTagMap = new HashMap<>();
//        grayTagMap.put(CloudGrayConstant.GRAY_HEADER, CloudGrayConstant.GRAY_VALUE);
//        final Set<Map.Entry<String,String>> attributes = Collections.unmodifiableSet(grayTagMap.entrySet());
//        Response<ServiceInstance> serviceInstanceResponse;
//        if (CloudGrayConstant.GRAY_VALUE.equals(grayTag)) {
//            log.info("【内部选择服务实例】- 灰度服务");
//            List<ServiceInstance> filteredInstances = Optional.ofNullable(serviceInstances).orElseGet(Lists::newArrayList)
//                    .stream()
//                    .filter(instance -> instance.getMetadata().entrySet().containsAll(attributes))
//                    .collect(Collectors.toList());
//            serviceInstanceResponse = getInstanceResponse(true, filteredInstances);
//        } else {
//            log.info("【内部选择服务实例】- 普通服务");
//            List<ServiceInstance> filteredInstances = Optional.ofNullable(serviceInstances).orElseGet(Lists::newArrayList)
//                    .stream()
//                    .filter(instance -> !instance.getMetadata().entrySet().containsAll(attributes))
//                    .collect(Collectors.toList());
//            serviceInstanceResponse = getInstanceResponse(false, filteredInstances);
//        }
//        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
//            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
//        }
//        return serviceInstanceResponse;
//    }
//
//    // RoundRobinLoadBalancer 轮询
//    private Response<ServiceInstance> getInstanceResponse(boolean isGray, List<ServiceInstance> instances) {
//        if (instances.isEmpty()) {
//            if (log.isWarnEnabled()) {
//                log.warn("No servers available for service: " + serviceId);
//            }
//            return new EmptyResponse();
//        }
//
//        // Do not move position when there is only 1 instance, especially some suppliers
//        // have already filtered instances
//        if (instances.size() == 1) {
//            return new DefaultResponse(instances.get(0));
//        }
//
//        // Ignore the sign bit, this allows pos to loop sequentially from 0 to
//        // Integer.MAX_VALUE
//        int pos = (isGray ? this.grayPosition.incrementAndGet() : this.normalPosition.incrementAndGet()) & Integer.MAX_VALUE;
//
//        ServiceInstance instance = instances.get(pos % instances.size());
//
//        return new DefaultResponse(instance);
//    }
//
//}
