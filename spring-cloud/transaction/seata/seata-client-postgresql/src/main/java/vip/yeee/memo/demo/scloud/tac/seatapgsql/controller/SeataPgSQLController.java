package vip.yeee.memo.demo.scloud.tac.seatapgsql.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.yeee.memo.base.model.rest.CommonResult;
import vip.yeee.memo.demo.scloud.tac.seatapgsql.biz.SeataPgSQLBiz;

import javax.annotation.Resource;

@RestController
public class SeataPgSQLController {

    @Resource
    private SeataPgSQLBiz seataPgSQLBiz;

    @GetMapping("/seata/exec/opr")
    public CommonResult<Void> seataExecOpr() {
        return CommonResult.success(seataPgSQLBiz.seataExecOpr());
    }

}
