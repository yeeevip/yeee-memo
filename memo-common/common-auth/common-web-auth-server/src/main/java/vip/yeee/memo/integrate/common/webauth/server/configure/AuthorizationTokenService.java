package vip.yeee.memo.integrate.common.webauth.server.configure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/25 13:46
 */
@Slf4j
public class AuthorizationTokenService extends DefaultTokenServices {

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        return super.readAccessToken(accessToken);
    }
}
