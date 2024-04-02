package vip.yeee.memo.common.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/7/12 14:02
 */
@RestController
@RequestMapping("common")
public class TestController {

    @GetMapping("test/interceptor-and-filter")
    public String testInterceptorAndFilter() {
        return "测试拦截器、过滤器。";
    }

}
