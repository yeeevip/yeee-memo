//package vip.yeee.memo.base.websecurityoauth2.configure;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.oauth2.provider.token.*;
//import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
//import vip.yeee.memo.base.websecurityoauth2.properties.AuthProperties;
//
//import javax.annotation.Resource;
//import javax.sql.DataSource;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
///**
// * description......
// * @author yeeee
// */
//@ConditionalOnBean(DataSource.class)
//@Configuration
//public class DbTokenStoreConfig {
//
//    @Resource
//    private DataSource dataSource;
//    @Resource
//    private AuthProperties authProperties;
//
//    //token保存策略
//    @Bean
//    public TokenStore dbTokenStore() {
//        return new JdbcTokenStore(dataSource);
//    }
//
//    @Bean
//    public AccessTokenConverter accessTokenConverter(){
//        DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
//        UserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter() {
//            @Override
//            public Map<String, ?> convertUserAuthentication(Authentication authentication) {
//                Map<String, Object> response = new LinkedHashMap<String, Object>();
//                response.put(USERNAME, authentication.getPrincipal());
//                if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
//                    response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
//                }
//                return response;
//            }
//        };
//        defaultAccessTokenConverter.setUserTokenConverter(userAuthenticationConverter);
//        return defaultAccessTokenConverter;
//    }
//
//}
