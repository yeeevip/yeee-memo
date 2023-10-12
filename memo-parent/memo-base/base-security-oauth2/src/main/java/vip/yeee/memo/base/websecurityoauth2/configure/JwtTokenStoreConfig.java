package vip.yeee.memo.base.websecurityoauth2.configure;

import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import vip.yeee.memo.base.websecurityoauth2.model.UserInfo;
import vip.yeee.memo.base.websecurityoauth2.properties.AuthProperties;

import javax.annotation.Resource;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * description......
 * @author yeeee
 */
@Configuration
public class JwtTokenStoreConfig {

    @Resource
    private AuthProperties authProperties;

    private final static String SIGN_ALGORITHM = "HMACSHA512";

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore((JwtAccessTokenConverter) accessTokenConverter());
    }

    @Bean
    public AccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        //配置JWT使用的秘钥
        jwtAccessTokenConverter.setSigningKey(authProperties.getJwtSecret());
        jwtAccessTokenConverter.setSigner(new MacSigner(SIGN_ALGORITHM
                , new SecretKeySpec(DatatypeConverter.parseBase64Binary(authProperties.getJwtSecret()), SIGN_ALGORITHM)));
        jwtAccessTokenConverter.setVerifier(new MacSigner(SIGN_ALGORITHM
                , new SecretKeySpec(DatatypeConverter.parseBase64Binary(authProperties.getJwtSecret()), SIGN_ALGORITHM)));

        UserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter() {
            @Override
            public Map<String, ?> convertUserAuthentication(Authentication authentication) {
                Map<String, Object> response = new LinkedHashMap<String, Object>();
                UserInfo userInfo = new UserInfo();
                BeanUtils.copyProperties(authentication.getPrincipal(), userInfo);
                response.put(USERNAME, userInfo);
                if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
                    response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
                }
                return response;
            }
        };
        ((DefaultAccessTokenConverter)jwtAccessTokenConverter.getAccessTokenConverter())
                .setUserTokenConverter(userAuthenticationConverter);
        return jwtAccessTokenConverter;
    }

//    @Bean
//    public JwtTokenEnhancer jwtTokenEnhancer() {
//        return new JwtTokenEnhancer();
//    }
//
//    public static class JwtTokenEnhancer implements TokenEnhancer {
//
//        @Override
//        public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
//            Map<String, Object> objectObjectHashMap = new HashMap<>();
//            objectObjectHashMap.put("enhance", "enhance info");
//            objectObjectHashMap.put("ceshi", "张三");
//            ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(objectObjectHashMap);
//            return oAuth2AccessToken;
//        }
//    }
}
