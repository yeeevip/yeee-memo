package vip.yeee.memo.demo.stools.kit.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/12/6 11:20
 */
@Slf4j
@RestController
public class SimpleToolsController {

    @RequestMapping("/stools/openapi/v1")
    public String openApiV1(@RequestParam String username) {
        return "收到请求 - 参数：" + username;
    }

    @PostMapping("/stools/openapi/v2")
    public String openApiV2(@RequestBody String body) {
        return "收到请求 - 参数：" + body;
    }

}
