package vip.yeee.memo.common.platformauth.server.configure;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import vip.yeee.memo.base.websecurityoauth2.constant.AuthConstant;
import vip.yeee.memo.base.websecurityoauth2.model.UserInfo;
import vip.yeee.memo.common.platformauth.server.properties.AuthTokenStoreProperties;

import javax.annotation.Resource;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;
import javax.xml.bind.DatatypeConverter;
import java.util.*;

/**
 * description......
 * @author https://www.yeee.vip
 */
@Configuration
public class TokenStoreConfig {

    @Resource
    private AuthTokenStoreProperties authTokenStoreProperties;
    @Resource
    private UserDetailsService userDetailsService;

    @ConditionalOnProperty(value = "yeee.webauth.token-store.db.enabled", havingValue = "true")
    @Configuration
    public class DbTokenStoreConfig {

        //token保存策略
        @Bean
        public TokenStore dbTokenStore(DataSource dataSource) {
            JdbcTokenStore jdbcTokenStore = new JdbcTokenStore(dataSource);
            jdbcTokenStore.setAuthenticationKeyGenerator(customAuthenticationKeyGenerator());
            return jdbcTokenStore;
        }

    }

    @ConditionalOnProperty(value = "yeee.webauth.token-store.redis.enabled", havingValue = "true")
    @Configuration
    public class RedisTokenStoreConfig {

        //token保存策略
        @Bean
        public TokenStore redisTokenStore(RedisConnectionFactory redisConnectionFactory) {
            RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
            redisTokenStore.setAuthenticationKeyGenerator(customAuthenticationKeyGenerator());
            return redisTokenStore;
        }

    }

    @ConditionalOnProperty(value = "yeee.webauth.token-store.jwt.enabled", havingValue = "true")
    @Configuration
    public class JwtTokenStoreConfig {

        @Bean
        public TokenStore jwtTokenStore() {
            JwtTokenStore jwtTokenStore = new JwtTokenStore((JwtAccessTokenConverter) accessTokenConverter());
            return jwtTokenStore;
        }
    }

    public AuthenticationKeyGenerator customAuthenticationKeyGenerator() {
        DefaultAuthenticationKeyGenerator keyGenerator = new DefaultAuthenticationKeyGenerator() {
            @Override
            public String extractKey(OAuth2Authentication authentication) {
                Map<String, String> values = new LinkedHashMap<>();
                OAuth2Request authorizationRequest = authentication.getOAuth2Request();
                if (!authentication.isClientOnly()) {
                    JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(authentication.getUserAuthentication().getPrincipal()));
                    values.put(AuthConstant.AUTH_USERNAME, jsonObject.getString("id"));
                }
                values.put(AuthConstant.AUTH_CLIENT_ID, authorizationRequest.getClientId());
                if (authorizationRequest.getScope() != null) {
                    values.put(AuthConstant.AUTH_SCOPE, OAuth2Utils.formatParameterList(new TreeSet<>(authorizationRequest.getScope())));
                }
                return generateKey(values);
            }
        };
        return keyGenerator;
    }

    @Bean
    public AccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        //配置JWT使用的秘钥
        jwtAccessTokenConverter.setSigningKey(authTokenStoreProperties.getJwtSecret());
        jwtAccessTokenConverter.setSigner(new MacSigner(AuthConstant.JWT_SIGN_ALGORITHM
                , new SecretKeySpec(DatatypeConverter.parseBase64Binary(authTokenStoreProperties.getJwtSecret()), AuthConstant.JWT_SIGN_ALGORITHM)));
        jwtAccessTokenConverter.setVerifier(new MacSigner(AuthConstant.JWT_SIGN_ALGORITHM
                , new SecretKeySpec(DatatypeConverter.parseBase64Binary(authTokenStoreProperties.getJwtSecret()), AuthConstant.JWT_SIGN_ALGORITHM)));

        UserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter() {
            @Override
            public Map<String, ?> convertUserAuthentication(Authentication authentication) {
                Map<String, Object> response = new LinkedHashMap<>();
                UserInfo userInfo = new UserInfo();
                BeanUtils.copyProperties(authentication.getPrincipal(), userInfo);
                response.put(AuthConstant.AUTH_USERNAME, userInfo.getUsername());
                response.put(AuthConstant.AUTH_USERID, userInfo.getId());
                response.put(AuthConstant.AUTH_USER_TYPE, userInfo.getUserType());
                return response;
            }

            @Override
            public Authentication extractAuthentication(Map<String, ?> map) {
                if (!authTokenStoreProperties.getJwt().getEnabled()) {
                    return super.extractAuthentication(map);
                }
                String username = (String) map.get(AuthConstant.AUTH_USERNAME);
                String userType = (String) map.get(AuthConstant.AUTH_USER_TYPE);
                if (StrUtil.isBlank(username) || StrUtil.isBlank(userType)) {
                    return null;
                }
                UserDetails userDetails = userDetailsService.loadUserByUsername(userType + AuthConstant.USERNAME_SEPARATOR + username);
                return new UsernamePasswordAuthenticationToken(userDetails, "N/A", userDetails.getAuthorities());
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
