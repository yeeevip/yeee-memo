package vip.yeee.memo.integrate.springcloud.register.feign.fallback.sentinel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.yeee.memo.integrate.springcloud.register.feign.fallback.sentinel.feign.TestFeignService;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/3 15:13
 */
@RestController
public class DemoServiceController {

    @Resource
    private TestFeignService testFeignService;

    @GetMapping("test/getData")
    public String getData() {
        return testFeignService.getData();
    }

}
