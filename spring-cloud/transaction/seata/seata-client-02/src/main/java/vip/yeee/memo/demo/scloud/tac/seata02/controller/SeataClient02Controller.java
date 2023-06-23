package vip.yeee.memo.demo.scloud.tac.seata02.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.yeee.memo.base.model.rest.CommonResult;
import vip.yeee.memo.demo.scloud.tac.seata02.biz.SeataClient02Biz;

import javax.annotation.Resource;

@RestController
public class SeataClient02Controller {

    @Resource
    private SeataClient02Biz seataClient02Biz;

    @GetMapping("/seata/exec/opr")
    public CommonResult<Void> testSeataService() {
        return CommonResult.success(seataClient02Biz.seataExecOpr());
    }

}
