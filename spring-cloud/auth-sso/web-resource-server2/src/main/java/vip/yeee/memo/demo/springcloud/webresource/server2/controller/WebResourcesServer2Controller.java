package vip.yeee.memo.demo.springcloud.webresource.server2.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.yeee.memo.base.model.annotation.AnonymousAccess;
import vip.yeee.memo.base.model.rest.CommonResult;
import vip.yeee.memo.demo.springcloud.webresource.server2.biz.WebResourcesServer2Biz;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/11/16 17:33
 */
@Api("资源1服务")
@RestController
public class WebResourcesServer2Controller {

    @Resource
    private WebResourcesServer2Biz webResourcesServer2Biz;

    @ApiOperation("资源API接口")
    @GetMapping("resources/api")
    public CommonResult<String> accessApi() {
        return CommonResult.success(webResourcesServer2Biz.accessApi());
    }

    @AnonymousAccess
    @GetMapping("anonymous/anno/api")
    public CommonResult<String> anonymousApiByAnno() {
        return CommonResult.success(webResourcesServer2Biz.anonymousApiByAnno());
    }

    @GetMapping("anonymous/limit/api")
    public CommonResult<String> anonymousApiByLimit() {
        return CommonResult.success(webResourcesServer2Biz.anonymousApiByLimit());
    }

}
