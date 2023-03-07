package vip.yeee.integrate.springcloud.webauth.server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vip.yeee.integrate.springcloud.webauth.server.biz.UserAuthBiz;
import vip.yeee.integrate.springcloud.webauth.server.model.request.UserAuthRequest;
import vip.yeee.memo.integrate.base.model.rest.CommonResult;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/16 17:33
 */
@RestController
public class UserAuthController {

    @Resource
    private UserAuthBiz userAuthBiz;

    @PostMapping("/inner/system/register")
    public CommonResult<Void> systemUserRegister(@RequestBody UserAuthRequest request) {
        return CommonResult.success(userAuthBiz.systemUserRegister(request));
    }

}
