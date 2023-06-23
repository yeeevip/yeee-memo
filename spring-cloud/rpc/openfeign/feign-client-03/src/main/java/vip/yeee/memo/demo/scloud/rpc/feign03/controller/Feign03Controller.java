package vip.yeee.memo.demo.scloud.rpc.feign03.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.yeee.memo.base.model.rest.CommonResult;
import vip.yeee.memo.demo.scloud.rpc.feign03.biz.Feign03Biz;

import javax.annotation.Resource;

@RestController
public class Feign03Controller {

    @Resource
    private Feign03Biz feign03Biz;

    @GetMapping("call/get-data")
    public CommonResult<String> getData() {
        return CommonResult.success(feign03Biz.getData());
    }

}
