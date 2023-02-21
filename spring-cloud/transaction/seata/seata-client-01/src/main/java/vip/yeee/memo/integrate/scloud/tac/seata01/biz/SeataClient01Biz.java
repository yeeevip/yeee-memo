package vip.yeee.memo.integrate.scloud.tac.seata01.biz;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.scloud.tac.seata01.domain.mysql.entity.TestEntity;
import vip.yeee.memo.integrate.scloud.tac.seata01.domain.mysql.service.TestEntityService;

import javax.annotation.Resource;
import java.util.Date;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/2/21 11:56
 */
@Slf4j
@Component
public class SeataClient01Biz {

    @Resource
    private TestEntityService testEntityService;

    public Void seataExecOpr() {
        TestEntity testEntity = new TestEntity();
        testEntity.setField2(DateUtil.format(new Date(), DatePattern.CHINESE_DATE_TIME_PATTERN));
        testEntityService.save(testEntity);
        log.info("seataExecOpr--->1 SUCCESS！！！");
        return null;
    }
}
