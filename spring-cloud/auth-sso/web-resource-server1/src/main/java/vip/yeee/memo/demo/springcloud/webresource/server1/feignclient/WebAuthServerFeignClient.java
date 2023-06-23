package vip.yeee.memo.demo.springcloud.webresource.server1.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vip.yeee.memo.base.model.rest.CommonResult;
import vip.yeee.memo.demo.springcloud.webresource.server1.model.request.UserAuthRequest;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/7 13:40
 */
@FeignClient(name = "cloud-web-auth-server", path = "/auth-server")
public interface WebAuthServerFeignClient {

    @PostMapping("/inner/system/register")
    CommonResult<Void> systemUserRegister(@RequestBody UserAuthRequest request);
}
