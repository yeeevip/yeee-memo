package vip.yeee.memo.integrate.seata.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.yeee.memo.integrate.base.model.rest.CommonResult;
import vip.yeee.memo.integrate.seata.biz.TestBiz;

@RequiredArgsConstructor
@RestController
@Slf4j
public class MainController {

    private final TestBiz testBiz;

    @GetMapping("test/seata/service")
    public CommonResult<Void> testSeataService() {
        testBiz.testSeataService();
        return CommonResult.success(null);
    }

}
