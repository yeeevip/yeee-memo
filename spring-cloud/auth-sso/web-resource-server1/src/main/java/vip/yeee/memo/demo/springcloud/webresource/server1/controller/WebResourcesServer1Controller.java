package vip.yeee.memo.demo.springcloud.webresource.server1.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.yeee.memo.base.model.annotation.AnonymousAccess;
import vip.yeee.memo.base.model.rest.CommonResult;
import vip.yeee.memo.demo.springcloud.webresource.server1.biz.WebResourcesServer1Biz;

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

    @PreAuthorize("hasAuthority('yeee:test:aaa')")
//    @PreAuthorize("hasAuthority('yeee:test:aaab')")
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
