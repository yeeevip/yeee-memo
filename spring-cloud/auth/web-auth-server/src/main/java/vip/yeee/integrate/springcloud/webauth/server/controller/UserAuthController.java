package vip.yeee.integrate.springcloud.webauth.server.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vip.yeee.integrate.springcloud.webauth.server.biz.UserAuthBiz;
import vip.yeee.integrate.springcloud.webauth.server.model.request.UserAuthRequest;
import vip.yeee.integrate.springcloud.webauth.server.model.vo.UserAuthVo;
import vip.yeee.memo.integrate.common.model.rest.CommonResult;
import vip.yeee.memo.integrate.common.websecurity.annotation.AnonymousAccess;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/16 17:33
 */
@Api("授权及认证")
@RestController
public class UserAuthController {

    @Resource
    private UserAuthBiz userAuthBiz;

    @PostMapping("system/register")
    public CommonResult<Void> systemUserRegister(@RequestBody UserAuthRequest request) {
        return CommonResult.success(userAuthBiz.systemUserRegister(request));
    }

    @PostMapping("system/login")
    public CommonResult<UserAuthVo> systemUserLogin(@RequestBody UserAuthRequest request) {
        return CommonResult.success(userAuthBiz.systemUserLogin(request));
    }

    @GetMapping("system/logout")
    public CommonResult<Void> userLogout() {
        return CommonResult.success(userAuthBiz.userLogout());
    }

    @GetMapping("system/api")
    public CommonResult<String> accessSystemApi() {
        return CommonResult.success(userAuthBiz.accessSystemApi());
    }

    @AnonymousAccess
    @GetMapping("anonymous/anno/api")
    public CommonResult<String> anonymousApiByAnno() {
        return CommonResult.success(userAuthBiz.anonymousApiByAnno());
    }

    @GetMapping("anonymous/limit/api")
    public CommonResult<String> anonymousApiByLimit() {
        return CommonResult.success(userAuthBiz.anonymousApiByLimit());
    }

}
