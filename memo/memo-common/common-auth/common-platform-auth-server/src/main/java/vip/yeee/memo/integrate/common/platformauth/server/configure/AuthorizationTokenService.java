package vip.yeee.memo.integrate.common.platformauth.server.configure;

import org.slf4j.Logger;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import vip.yeee.memo.integrate.base.util.LogUtils;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/25 13:46
 */
public class AuthorizationTokenService extends DefaultTokenServices {

    private final static Logger log = LogUtils.commonAuthLog();

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        return super.readAccessToken(accessToken);
    }
}
