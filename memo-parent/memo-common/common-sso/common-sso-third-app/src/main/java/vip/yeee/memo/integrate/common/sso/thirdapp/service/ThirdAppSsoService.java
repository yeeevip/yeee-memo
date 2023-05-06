package vip.yeee.memo.integrate.common.sso.thirdapp.service;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.google.common.collect.Maps;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Service;
import vip.yeee.memo.integrate.base.util.JacksonUtils;
import vip.yeee.memo.integrate.base.websecurityoauth2.model.AuthUser;
import vip.yeee.memo.integrate.common.sso.thirdapp.model.dto.AuthUserDto;
import vip.yeee.memo.integrate.common.sso.thirdapp.model.dto.ThirdAppDto;
import vip.yeee.memo.integrate.common.sso.thirdapp.model.dto.ThirdUserDto;
import vip.yeee.memo.integrate.common.sso.thirdapp.model.request.ThirdAppSsoRequest;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/3/20 18:48
 */
@Service
public class ThirdAppSsoService {

    @Resource
    private OAuth2RestTemplate oAuth2RestTemplate;
    @Resource
    private ResourceServerTokenServices resourceServerTokenServices;

    public void thirdLoginV2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ThirdAppSsoRequest ssoRequest = getRequestParams(request);
        try {
            // 验签
            checkSign(ssoRequest);
            // 从数据库获取三方应用信息
            ThirdAppDto thirdAppDto = getAppByAppKey(ssoRequest.getAppKey());
            // 检查应用
            checkApp(thirdAppDto);
            // 根据ticket从三方获取用户信息
            ThirdUserDto thirdUserDto = getThirdUserDetailByTicket(thirdAppDto, ssoRequest.getTicket());
            // 根据手机号查询或者注册用户
            AuthUserDto authUserDto = findOrInsertThirdUser(thirdUserDto);
            // 内部登录认证
            OAuth2AccessToken accessToken = innerLoginAuth(authUserDto);
            Cookie cookie = new Cookie("token", accessToken.getValue());
            cookie.setPath("/");
            cookie.setDomain("");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            OAuth2Authentication oAuth2Authentication = resourceServerTokenServices.loadAuthentication(accessToken.getValue());
            AuthUser authUser = (AuthUser) oAuth2Authentication.getPrincipal();
            authUser.getUserId();
            authUser.getPermissions();

            response.sendRedirect(ssoRequest.getAuthSuccessUrl());
        } catch (Exception e) {
            response.sendRedirect(ssoRequest.getAuthFailUrl());
        }

    }

    private AuthUserDto findOrInsertThirdUser(ThirdUserDto thirdUserDto) {
//        AuthUserDto userDto = userDao.queryByPhone(thirdUserDto.getPhone());
//        if (userDto == null) {
//            userDto = new AuthUserDto();
//            userDao.insert(userDto);
//        }
        return null;
    }

    private ThirdUserDto getThirdUserDetailByTicket(ThirdAppDto thirdAppDto, String ticket) {
        // 生成签名
        String sign = SecureUtil.md5(thirdAppDto.getAppKey() + thirdAppDto.getAppSecret());
        Map<String, Object> reqParams = Maps.newHashMap();
        reqParams.put("ticket", ticket);
        reqParams.put("timestamps", String.valueOf(System.currentTimeMillis()));
        reqParams.put("sign", sign);
        String response = HttpUtil.post(thirdAppDto.getTicketUrl(), reqParams);
        try {
            return JacksonUtils.toJavaBean(response, ThirdUserDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void checkApp(ThirdAppDto thirdAppDto) {

    }

    private ThirdAppDto getAppByAppKey(String appKey) {
        return null;
    }

    private ThirdAppSsoRequest getRequestParams(HttpServletRequest request) {
        return null;
    }

    private void checkSign(ThirdAppSsoRequest ssoRequest) {

    }

    private OAuth2AccessToken innerLoginAuth(AuthUserDto authUserDto) {
        ResourceOwnerPasswordResourceDetails passwordResourceDetails =
                (ResourceOwnerPasswordResourceDetails) this.oAuth2RestTemplate.getResource();
        passwordResourceDetails.setUsername(authUserDto.getUsername());
        passwordResourceDetails.setPassword(authUserDto.getPassword());
        oAuth2RestTemplate.getOAuth2ClientContext().setAccessToken(null);
        OAuth2AccessToken accessToken = oAuth2RestTemplate.getAccessToken();
        return accessToken;
    }

    public Object getUserInfoByTicket(HttpServletRequest request) {
        String ticket = request.getParameter("ticket");
        String sign = request.getParameter("sign");
        // 验签
//        checkInfoSign(request, sign);
        HashMap<String, Object> res = Maps.newHashMap();
        res.put("username", "用户名");
        res.put("phone", "手机号");
        return res;
    }
}
