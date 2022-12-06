package vip.yeee.integrate.springcloud.webauth.server.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import vip.yeee.memo.integrate.base.model.rest.CommonResult;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/7 13:40
 */
@FeignClient(name = "cloud-web-auth-resource1", path = "/auth-resource1")
public interface WebAuthResource1FeignClient {

    @GetMapping("anonymous/anno/api")
    CommonResult<String> anonymousApiByAnno();

}
