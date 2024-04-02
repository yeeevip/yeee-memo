package vip.yeee.memo.demo.scloud.rpc.feign03.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import vip.yeee.memo.demo.scloud.rpc.feign03.feign.fallback.TestFeignFallback;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/3 15:14
 */
@FeignClient(name = "feign-service-server", path = "/fss", fallback = TestFeignFallback.class)
public interface TestFeignService {

    @GetMapping("test/get/data")
    String getData();

}
