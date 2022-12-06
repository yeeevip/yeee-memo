//package vip.yeee.memo.integrate.base.webauth.server.configure;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//import vip.yeee.memo.integrate.base.webauth.server.properties.AuthProperties;
//
//import javax.annotation.Resource;
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
//        return new JwtTokenStore(jwtAccessTokenConverter());
//    }
//
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
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
//}
