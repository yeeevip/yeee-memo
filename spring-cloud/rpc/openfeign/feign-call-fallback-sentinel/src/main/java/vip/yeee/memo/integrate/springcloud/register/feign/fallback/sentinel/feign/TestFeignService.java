package vip.yeee.memo.integrate.springcloud.register.feign.fallback.sentinel.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import vip.yeee.memo.integrate.springcloud.register.feign.fallback.sentinel.feign.fallback.TestFeignFallback;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/3 15:14
 */
@FeignClient(name = "target-service", path = "/target", fallback = TestFeignFallback.class)
public interface TestFeignService {

    @GetMapping("test/get/data")
    String getData();

}
