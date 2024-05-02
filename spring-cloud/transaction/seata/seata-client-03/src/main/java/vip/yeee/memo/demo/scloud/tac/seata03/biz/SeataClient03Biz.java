package vip.yeee.memo.demo.scloud.tac.seata03.biz;

import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.base.model.rest.CommonResult;
import vip.yeee.memo.demo.scloud.tac.seata03.feign.SeataClient02FeignClient;
import vip.yeee.memo.demo.scloud.tac.seata03.feign.SeataClient01FeignClient;
import vip.yeee.memo.demo.scloud.tac.seata03.feign.SeataClientPgSQLFeignClient;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/18 18:29
 */
@Slf4j
@Component
public class SeataClient03Biz {

    @Resource
    private SeataClient01FeignClient seataClient01FeignClient;
    @Resource
    private SeataClient02FeignClient seataClient02FeignClient;
    @Resource
    private SeataClientPgSQLFeignClient seataClientPgSQLFeignClient;

    @GlobalTransactional(rollbackFor = Exception.class)
    public Void seataExecOpr() {
        seataClient01FeignClient.seataExecOpr();
        seataClient02FeignClient.seataExecOpr();
        log.info("seataExecOpr--->3 SUCCESS！！！");
        return null;
    }

    @GlobalTransactional(rollbackFor = Exception.class)
    public Void seataExecMixDatabaseOpr() {
        // 保存postgresql数据库
        CommonResult<Void> oprRes2 = seataClientPgSQLFeignClient.seataExecOpr();
        if (oprRes2.getCode() != 200) {
            throw new RuntimeException();
        }
        // 保存mysql数据库
        CommonResult<Void> oprRes1 = seataClient01FeignClient.seataExecOpr();
        if (oprRes1.getCode() != 200) {
            throw new RuntimeException();
        }
        log.info("seataExecOpr--->mix SUCCESS！！！");
        return null;
    }
}
