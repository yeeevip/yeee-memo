package vip.yeee.memo.common.platformauth.server.configure;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import vip.yeee.memo.base.util.LogUtils;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/25 13:46
 */
@Component
public class CustomTokenService extends DefaultTokenServices {

    private final static Logger log = LogUtils.commonAuthLog();

    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(20);

    private static final Charset US_ASCII = Charset.forName("US-ASCII");

    @Resource
    private TokenStore tokenStore;
    @Autowired(required = false)
    private TokenEnhancer accessTokenEnhancer;

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        return super.readAccessToken(accessToken);
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessTokenValue) throws AuthenticationException, InvalidTokenException {
        return super.loadAuthentication(accessTokenValue);
    }

    @Override
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
        OAuth2AccessToken existingAccessToken = tokenStore.getAccessToken(authentication);
        if (existingAccessToken != null) {
            tokenStore.removeAccessToken(existingAccessToken);
            if (existingAccessToken.getRefreshToken() != null) {
                tokenStore.removeRefreshToken(existingAccessToken.getRefreshToken());
            }
        }
        OAuth2RefreshToken refreshToken = createRefreshToken(authentication);
        OAuth2AccessToken accessToken = createAccessToken(authentication, refreshToken);
        tokenStore.storeAccessToken(accessToken, authentication);
        tokenStore.storeRefreshToken(refreshToken, authentication);
        return accessToken;
    }

    private OAuth2RefreshToken createRefreshToken(OAuth2Authentication authentication) {
        if (!isSupportRefreshToken(authentication.getOAuth2Request())) {
            return null;
        }
        int validitySeconds = getRefreshTokenValiditySeconds(authentication.getOAuth2Request());
        String tokenValue = new String(Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey()), US_ASCII);
        if (validitySeconds > 0) {
            return new DefaultExpiringOAuth2RefreshToken(tokenValue, new Date(System.currentTimeMillis()
                    + (validitySeconds * 1000L)));
        }
        return new DefaultOAuth2RefreshToken(tokenValue);
    }

    private OAuth2AccessToken createAccessToken(OAuth2Authentication authentication, OAuth2RefreshToken refreshToken) {
        String tokenValue = new String(Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey()),  US_ASCII);
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(tokenValue);
        int validitySeconds = getAccessTokenValiditySeconds(authentication.getOAuth2Request());
        if (validitySeconds > 0) {
            token.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
        }
        token.setRefreshToken(refreshToken);
        token.setScope(authentication.getOAuth2Request().getScope());

        return accessTokenEnhancer != null ? accessTokenEnhancer.enhance(token, authentication) : token;
    }
}
