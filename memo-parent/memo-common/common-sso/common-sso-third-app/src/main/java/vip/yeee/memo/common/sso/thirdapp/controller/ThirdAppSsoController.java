package vip.yeee.memo.common.sso.thirdapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.yeee.memo.base.model.rest.CommonResult;
import vip.yeee.memo.common.sso.thirdapp.service.ThirdAppSsoService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 三方网站单点登录本平台；
 * 仅需登录一次
 * 目标：他的用户在他的系统直接可以使用我们的平台服务
 *
 * @author https://www.yeee.vip
 * @since 2023/3/20 18:47
 */
@RestController
public class ThirdAppSsoController {

    @Resource
    private ThirdAppSsoService thirdAppSsoService;

    /**
     * 基于spring security 过滤器 拦截【/third/v1/login】处理授权认证
     */
    @RequestMapping("/third/v1/login")
    public void thirdLoginV1(HttpServletRequest request, HttpServletResponse response) {

    }

    /**
     *
     */
    @RequestMapping("/third/v2/login")
    public void thirdLoginV2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        thirdAppSsoService.thirdLoginV2(request, response);
    }

    /**
     * 根据ticket获取三方应用的当前用户信息
     */
    @RequestMapping("/third/user/info")
    public CommonResult<Object> getUserInfoByTicket(HttpServletRequest request) {
        return CommonResult.success(thirdAppSsoService.getUserInfoByTicket(request));
    }


}
