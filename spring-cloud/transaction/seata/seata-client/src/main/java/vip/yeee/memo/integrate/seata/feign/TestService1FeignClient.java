package vip.yeee.memo.integrate.seata.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import vip.yeee.memo.integrate.common.model.rest.CommonResult;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/18 18:33
 */
@FeignClient(name = "feign-service-client", path = "/fsc")
public interface TestService1FeignClient {

    @GetMapping("test/seata/service1")
    CommonResult<Void> testSeataService1();

}
