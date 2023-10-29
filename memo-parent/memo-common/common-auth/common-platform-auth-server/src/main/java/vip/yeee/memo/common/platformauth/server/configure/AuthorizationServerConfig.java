package vip.yeee.memo.common.platformauth.server.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.token.*;
import vip.yeee.memo.common.platformauth.server.constant.SecurityConstants;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 认证服务器配置
 *
 * @author yeeee
 * @since 2022/4/28 16:25
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Resource
    private DataSource dataSource;
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private TokenStore tokenStore;
    @Resource
    private AccessTokenConverter accessTokenConverter;

    // 从数据库中查询出客户端信息
    @Bean
    public JdbcClientDetailsService clientDetailsService() {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
        clientDetailsService.setFindClientDetailsSql(SecurityConstants.DEFAULT_FIND_STATEMENT);
        return clientDetailsService;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //从数据库取数据
        clients.withClientDetails(clientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // 允许form表单客户端认证,允许客户端使用client_id和client_secret获取token
                .allowFormAuthenticationForClients()
                // 通过验证返回token信息
                .checkTokenAccess("isAuthenticated()")
                // 获取token请求不进行拦截
                .tokenKeyAccess("permitAll()")
                //获取密钥需要身份认证,使用单点登录时必须配置
//                .tokenKeyAccess("isAuthenticated()")
        ;

    }

    // 使用密码模式所需配置
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

//        // 配置JWT的内容增强器
//        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
//        List<TokenEnhancer> delegates = new ArrayList<>();
//        delegates.add(jwtTokenEnhancer);
//        delegates.add((TokenEnhancer) accessTokenConverter);
//        enhancerChain.setTokenEnhancers(delegates);

        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenServices(customTokenService(tokenStore, clientDetailsService()))
                //配置存储令牌策略
                .tokenStore(tokenStore)
                .accessTokenConverter(accessTokenConverter)
//                .tokenEnhancer(enhancerChain)
        ;
    }

    @Bean
    public CustomTokenService customTokenService(TokenStore tokenStore, ClientDetailsService clientDetailsService) {
        CustomTokenService customTokenService = new CustomTokenService();
        customTokenService.setTokenStore(tokenStore);
        customTokenService.setClientDetailsService(clientDetailsService);
        return customTokenService;
    }

    /**
     * check_token端点重写
     */
    @Bean
    public CheckTokenEndpoint checkTokenEndpoint(CustomTokenService customTokenService) {
        CheckTokenEndpoint checkTokenEndpoint = new CheckTokenEndpoint(customTokenService);

        DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
        UserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter() {
            @Override
            public Map<String, ?> convertUserAuthentication(Authentication authentication) {
                Map<String, Object> response = new LinkedHashMap<>();
                response.put(USERNAME, authentication.getPrincipal());
                if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
                    response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
                }
                return response;
            }

        };
        defaultAccessTokenConverter.setUserTokenConverter(userAuthenticationConverter);

        checkTokenEndpoint.setAccessTokenConverter(defaultAccessTokenConverter);
//        checkTokenEndpoint.setExceptionTranslator(cloudWebResponseExceptionTranslator);
        return checkTokenEndpoint;
    }

    @ConfigurationProperties(prefix = "security.oauth2.client")
	@Bean
	public OAuth2ProtectedResourceDetails resourceOwnerPasswordResourceDetails() {
		return new ResourceOwnerPasswordResourceDetails();
	}

}
