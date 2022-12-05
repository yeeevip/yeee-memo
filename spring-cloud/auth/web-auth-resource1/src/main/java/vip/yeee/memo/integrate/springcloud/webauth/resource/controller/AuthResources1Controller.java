package vip.yeee.memo.integrate.springcloud.webauth.resource.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.yeee.memo.integrate.base.model.rest.CommonResult;
import vip.yeee.memo.integrate.base.websecurity.annotation.AnonymousAccess;
import vip.yeee.memo.integrate.springcloud.webauth.resource.biz.AuthResources1Biz;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/16 17:33
 */
@Api("资源1服务")
@RestController
public class AuthResources1Controller {

    @Resource
    private AuthResources1Biz authResources1Biz;

    @ApiOperation("资源API接口")
    @GetMapping("resources/api")
    public CommonResult<String> accessApi() {
        return CommonResult.success(authResources1Biz.accessApi());
    }

    @AnonymousAccess
    @GetMapping("anonymous/anno/api")
    public CommonResult<String> anonymousApiByAnno() {
        return CommonResult.success(authResources1Biz.anonymousApiByAnno());
    }

    @GetMapping("anonymous/limit/api")
    public CommonResult<String> anonymousApiByLimit() {
        return CommonResult.success(authResources1Biz.anonymousApiByLimit());
    }

}
