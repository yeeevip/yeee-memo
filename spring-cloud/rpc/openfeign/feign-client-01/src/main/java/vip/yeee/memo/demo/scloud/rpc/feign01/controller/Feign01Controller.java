package vip.yeee.memo.demo.scloud.rpc.feign01.controller;

import org.springframework.web.bind.annotation.*;
import vip.yeee.memo.base.model.rest.CommonResult;

@RestController
public class Feign01Controller {

    @GetMapping("call/get-data")
    public CommonResult<String> getData() {
        return CommonResult.success("Hello");
    }

}
