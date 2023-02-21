package vip.yeee.memo.integrate.scloud.rpc.feign03.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import vip.yeee.memo.integrate.base.model.rest.CommonResult;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/2/21 14:03
 */
@FeignClient(name = "feign-client-01", path = "/feign01")
public interface Feign01FeignClient {

    @GetMapping("call/get-data")
    CommonResult<String> getData();
}
