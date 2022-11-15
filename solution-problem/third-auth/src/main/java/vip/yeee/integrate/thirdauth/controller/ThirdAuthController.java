package vip.yeee.integrate.thirdauth.controller;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.google.common.collect.ImmutableMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.yeee.memo.integrate.common.model.exception.BizException;
import vip.yeee.memo.integrate.common.model.rest.CommonResult;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequestMapping("third")
@RestController
public class ThirdAuthController {

    private final static TimedCache<String, Object> CODE_CACHE = CacheUtil.newTimedCache(TimeUnit.MINUTES.toMillis(1));
    private final static String ALLOW_DOMAIN = "yeee.vip";

    /************************* 【三方应用获取Token调用接口】*************************************************/
    @PostMapping("api/token")
    public CommonResult<Object> getToken(@RequestParam String appId, @RequestParam String appSecret) {
        String token = SecureUtil.md5().digestHex(appId + appSecret);
        Map<String, Object> res = ImmutableMap.of("token", token, "expire", TimeUnit.HOURS.toSeconds(2));
        return CommonResult.success(res);
    }
    /******************************************************************************************************/

    /************************ 【不同应用跳转-免登录】-【Cookie方式】********************************************/
    @RequestMapping("app/jump-page")
    public void jumpPage(@RequestParam String url, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!url.contains(ALLOW_DOMAIN)) {
            throw new BizException("链接非法");
        }
        String authorization = request.getHeader("Authorization");
        if (StrUtil.isBlank(authorization)) {
            throw new BizException("请登录");
        }
        // 将令牌传给跳转链接
        Cookie tokenCookie = new Cookie("Authorization", authorization);
        tokenCookie.setPath("/");
        tokenCookie.setMaxAge(-1); // 浏览器关闭就消失
        tokenCookie.setDomain(ALLOW_DOMAIN);
        response.addCookie(tokenCookie);
        response.sendRedirect(url);
    }
    /****************************************************************************************************************/


    /************************* 【三方应用获取用户信息】-【授权码方式】2.1、用户授权三方-生成授权码 **************************************/
    @RequestMapping("app/auth-code/1")
    public void redirectAuthCode(@RequestParam String url, HttpServletResponse response) throws IOException {
        response.sendRedirect(url + "?code=" + this.genAuthCode());
    }

    @RequestMapping("app/auth-code/2")
    public CommonResult<Object> getAuthCode(@RequestParam String url) {
        Map<String, String> res = ImmutableMap.of("redirectUrl", url + "?code=" + this.genAuthCode());
        return CommonResult.success(res);
    }

    private String genAuthCode() {
        // 当前用户
        Map<String, Object> userInfo = ImmutableMap.of("userId", Integer.valueOf(RandomUtil.randomNumbers(5)));
        String authCode = IdUtil.simpleUUID() + RandomUtil.randomString(4);
        CODE_CACHE.put(authCode, userInfo);
        return authCode;
    }
    /********************************************************************************************************************************/


    /************************ 【三方应用获取用户信息】-【授权码方式】2.2、 三方通过授权码获取用户信息 ***********************************/
    @RequestMapping("app/user-info")
    public CommonResult<Object> getToken(@RequestParam String code) {
        if (!CODE_CACHE.containsKey(code)) {
            throw new BizException("无效的code");
        }
        Map<String, Object> res = ImmutableMap.of("info", CODE_CACHE.get(code));
        return CommonResult.success(res);
    }
    /*********************************************************************************************************************************/

}
