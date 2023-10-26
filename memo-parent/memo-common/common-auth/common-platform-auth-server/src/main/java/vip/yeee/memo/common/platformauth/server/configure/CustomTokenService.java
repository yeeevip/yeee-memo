package vip.yeee.memo.common.platformauth.server.configure;

import org.slf4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import vip.yeee.memo.base.util.LogUtils;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/25 13:46
 */
public class CustomTokenService extends DefaultTokenServices {

    private final static Logger log = LogUtils.commonAuthLog();

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        return super.readAccessToken(accessToken);
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessTokenValue) throws AuthenticationException, InvalidTokenException {
        return super.loadAuthentication(accessTokenValue);
    }
}
