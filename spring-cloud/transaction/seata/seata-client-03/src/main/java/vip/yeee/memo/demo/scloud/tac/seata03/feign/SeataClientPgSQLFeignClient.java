package vip.yeee.memo.demo.scloud.tac.seata03.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import vip.yeee.memo.base.model.rest.CommonResult;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/18 18:33
 */
@FeignClient(name = "seata-client-postgresql", path = "/clientpgsql")
public interface SeataClientPgSQLFeignClient {

    @GetMapping("/seata/exec/opr")
    CommonResult<Void> seataExecOpr();

}
