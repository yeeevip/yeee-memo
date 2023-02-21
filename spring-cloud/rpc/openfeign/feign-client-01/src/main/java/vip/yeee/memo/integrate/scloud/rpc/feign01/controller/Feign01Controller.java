package vip.yeee.memo.integrate.scloud.rpc.feign01.controller;

import org.springframework.web.bind.annotation.*;
import vip.yeee.memo.integrate.base.model.rest.CommonResult;

@RestController
public class Feign01Controller {

    @GetMapping("call/get-data")
    public CommonResult<String> getData() {
        return CommonResult.success("Hello");
    }

}
