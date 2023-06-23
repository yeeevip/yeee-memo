package vip.yeee.memo.demo.scloud.tac.seata01.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.yeee.memo.base.model.rest.CommonResult;
import vip.yeee.memo.demo.scloud.tac.seata01.biz.SeataClient01Biz;

import javax.annotation.Resource;

@RestController
public class SeataClient01Controller {

    @Resource
    private SeataClient01Biz seataClient01Biz;

    @GetMapping("/seata/exec/opr")
    public CommonResult<Void> seataExecOpr() {
        return CommonResult.success(seataClient01Biz.seataExecOpr());
    }

}
