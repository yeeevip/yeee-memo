package vip.yeee.memo.demo.scloud.tac.seatapgsql.biz;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.demo.scloud.tac.seatapgsql.domain.pgsql.entity.Test1;
import vip.yeee.memo.demo.scloud.tac.seatapgsql.domain.pgsql.service.Test1Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * description......
 *
 * @author yeeee
 * @since 2024/5/3 0:39
 */
@Slf4j
@Component
public class SeataPgSQLBiz {

    @Resource
    private Test1Service test1Service;

    public Void seataExecOpr() {
        Test1 test1 = new Test1();
        test1.setId(IdUtil.getSnowflake(1, 1).nextId());
        test1.setName(RandomUtil.randomString(6));
        test1Service.save(test1);
        log.info("seataExecOpr--->pgsql SUCCESS！！！");
        return null;
    }
}
