package vip.yeee.memo.integrate.springcloud.webresource.server1.service;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.base.websecurityoauth2.constant.AuthConstant;
import vip.yeee.memo.integrate.base.websecurityoauth2.constant.MessageConstant;
import vip.yeee.memo.integrate.base.websecurityoauth2.model.Oauth2TokenVo;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/3/8 10:58
 */
@Slf4j
@Component
public class WebAuthService {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private TokenStore tokenStore;
    @Resource
    private OAuth2RestTemplate oAuth2RestTemplate;
    private final static String SPLIT_PATTERN = "##";

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public String decodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean matchPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public Oauth2TokenVo getUserAccessToken(String username, String password, String userType) {
        log.info("getUserAccessToken, userType = {}, username = {}, password = {}", userType, username, password);
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            throw new BizException(MessageConstant.USERNAME_PASSWORD_ERROR);
        }
        ResourceOwnerPasswordResourceDetails passwordResourceDetails =
                (ResourceOwnerPasswordResourceDetails) this.oAuth2RestTemplate.getResource();
        passwordResourceDetails.setUsername(userType + SPLIT_PATTERN + username);
        passwordResourceDetails.setPassword(password);
        oAuth2RestTemplate.getOAuth2ClientContext().setAccessToken(null);
        OAuth2AccessToken accessToken = oAuth2RestTemplate.getAccessToken();
        Oauth2TokenVo oauth2TokenVo = new Oauth2TokenVo();
        oauth2TokenVo.setToken(accessToken.getValue());
        oauth2TokenVo.setRefreshToken(accessToken.getRefreshToken().getValue());
        oauth2TokenVo.setExpiresIn(accessToken.getExpiresIn());
        oauth2TokenVo.setTokenHead(AuthConstant.JWT_TOKEN_PREFIX);
        return oauth2TokenVo;
    }

    public Object userLogout(String token) {
        log.info("logout");
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
        // 移除access_token
        tokenStore.removeAccessToken(accessToken);
        // 移除refresh_token
        if (accessToken.getRefreshToken() != null) {
            tokenStore.removeRefreshToken(accessToken.getRefreshToken());
        }
        return null;
    }

}
