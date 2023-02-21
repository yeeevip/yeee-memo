package vip.yeee.memo.integrate.scloud.tac.seata03.biz;

import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.scloud.tac.seata03.feign.SeataClient01FeignClient;
import vip.yeee.memo.integrate.scloud.tac.seata03.feign.SeataClient02FeignClient;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/18 18:29
 */
@Slf4j
@Component
public class SeataClient03Biz {

    @Resource
    private SeataClient01FeignClient seataClient01FeignClient;
    @Resource
    private SeataClient02FeignClient seataClient02FeignClient;

    @GlobalTransactional(rollbackFor = Exception.class)
    public Void seataExecOpr() {
        seataClient01FeignClient.seataExecOpr();
        seataClient02FeignClient.seataExecOpr();
        log.info("seataExecOpr--->3 SUCCESS！！！");
        return null;
    }

}
