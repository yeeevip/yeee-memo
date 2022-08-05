package vip.yeee.memo.integrate.springcloud.register.feign.fallback.sentinel.controller;

import org.springframework.web.bind.annotation.*;
import vip.yeee.memo.integrate.springcloud.register.feign.fallback.sentinel.feign.TestFeignService;
import vip.yeee.memo.integrate.springcloud.register.feign.fallback.sentinel.model.TestReqVO;

import javax.annotation.Resource;
import java.time.LocalDateTime;

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

    @PostMapping("test/jsonFormat")
    public LocalDateTime testAnnotationJsonFormat(@RequestBody TestReqVO reqVO) {
        return reqVO.getStartDate();
    }

    @RequestMapping("test/dateTimeFormat")
    public LocalDateTime testAnnotationDateTimeFormat(TestReqVO reqVO) {
        return reqVO.getEndDate();
    }

}
