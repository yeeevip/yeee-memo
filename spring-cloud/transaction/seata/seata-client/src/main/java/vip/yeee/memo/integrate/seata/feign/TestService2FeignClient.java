package vip.yeee.memo.integrate.seata.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import vip.yeee.memo.integrate.base.model.rest.CommonResult;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/18 18:33
 */
@FeignClient(name = "feign-service-server", path = "/fss")
public interface TestService2FeignClient {

    @GetMapping("test/seata/service2")
    CommonResult<Void> testSeataService2();

}
