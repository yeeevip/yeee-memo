//package vip.yeee.memo.base.websecurityoauth2.configure;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.TokenEnhancer;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//import vip.yeee.memo.base.websecurityoauth2.properties.AuthProperties;
//
//import javax.annotation.Resource;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * description......
// * @author yeeee
// */
//@Configuration
//public class JwtTokenStoreConfig {
//
//    @Resource
//    private AuthProperties authProperties;
//
//    @Bean
//    public TokenStore jwtTokenStore() {
//        return new JwtTokenStore((JwtAccessTokenConverter) jwtAccessTokenConverter());
//    }
//
//    @Bean
//    public AccessTokenConverter accessTokenConverter() {
//        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
//        //配置JWT使用的秘钥
//        jwtAccessTokenConverter.setSigningKey(authProperties.getJwtSecret());
//        return jwtAccessTokenConverter;
//    }
//
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
//}
