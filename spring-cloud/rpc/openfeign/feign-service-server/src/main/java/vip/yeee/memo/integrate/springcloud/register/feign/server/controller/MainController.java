package vip.yeee.memo.integrate.springcloud.register.feign.server.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vip.yeee.memo.integrate.base.model.rest.CommonResult;
import vip.yeee.memo.integrate.springcloud.register.feign.server.domain.mysql.entity.TestEntity;
import vip.yeee.memo.integrate.springcloud.register.feign.server.domain.mysql.service.TestEntityService;

import java.util.Date;

@RequiredArgsConstructor
@RestController
@Slf4j
public class MainController {

    private final TestEntityService testEntityService;

    @GetMapping("test/seata/service2")
    public CommonResult<Void> testSeataService2() {
        TestEntity testEntity = new TestEntity();
        testEntity.setField2(DateUtil.format(new Date(), DatePattern.CHINESE_DATE_TIME_PATTERN));
        testEntityService.save(testEntity);
        log.info("testSeataService2 SUCCESS！！！");
        return CommonResult.success(null);
    }

}
