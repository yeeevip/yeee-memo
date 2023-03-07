package vip.yeee.memo.integrate.springcloud.webresource.server1.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vip.yeee.memo.integrate.base.model.rest.CommonResult;
import vip.yeee.memo.integrate.base.websecurityoauth2.annotation.AnonymousAccess;
import vip.yeee.memo.integrate.springcloud.webresource.server1.biz.WebResourcesServer1Biz;
import vip.yeee.memo.integrate.springcloud.webresource.server1.model.request.UserAuthRequest;
import vip.yeee.memo.integrate.springcloud.webresource.server1.model.vo.UserAuthVo;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/16 17:33
 */
@Api("资源1服务")
@RestController
public class WebResourcesServer1Controller {

    @Resource
    private WebResourcesServer1Biz webResourcesServer1Biz;

    @PostMapping("/system/register")
    public CommonResult<Void> systemUserRegister(@RequestBody UserAuthRequest request) {
        return CommonResult.success(webResourcesServer1Biz.systemUserRegister(request));
    }

    @PostMapping("/system/login")
    public CommonResult<UserAuthVo> systemUserLogin(@RequestBody UserAuthRequest request) {
        return CommonResult.success(webResourcesServer1Biz.systemUserLogin(request));
    }

    @GetMapping("/system/logout")
    public CommonResult<Void> userLogout() {
        return CommonResult.success(webResourcesServer1Biz.userLogout());
    }

    @ApiOperation("资源API接口")
    @GetMapping("resources/api")
    public CommonResult<String> accessApi() {
        return CommonResult.success(webResourcesServer1Biz.accessApi());
    }

    @AnonymousAccess
    @GetMapping("anonymous/anno/api")
    public CommonResult<String> anonymousApiByAnno() {
        return CommonResult.success(webResourcesServer1Biz.anonymousApiByAnno());
    }

    @GetMapping("anonymous/limit/api")
    public CommonResult<String> anonymousApiByLimit() {
        return CommonResult.success(webResourcesServer1Biz.anonymousApiByLimit());
    }

}
