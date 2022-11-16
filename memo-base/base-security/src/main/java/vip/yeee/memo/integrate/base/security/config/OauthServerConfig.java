package vip.yeee.memo.integrate.base.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import vip.yeee.memo.integrate.base.security.service.CustomUserDetailsService;
import vip.yeee.memo.integrate.base.security.constant.SecurityConstants;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * OAuth2授权配置类
 *
 * @author yeeee
 * @since 2022/4/28 16:25
 */
@Configuration
@EnableAuthorizationServer
public class OauthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Resource
    private DataSource dataSource;

    @Resource
    private CustomUserDetailsService userDetailsService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private PasswordEncoder passwordEncoder;

    // 从数据库中查询出客户端信息
    @Bean
    public JdbcClientDetailsService clientDetailsService() {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
        clientDetailsService.setFindClientDetailsSql(SecurityConstants.DEFAULT_FIND_STATEMENT);
        return clientDetailsService;
    }

    //token保存策略
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    //授权信息保存策略
    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    // 授权码模式专用对象
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        //从数据库取数据
        clients.withClientDetails(clientDetailsService());

        // 从内存中取数据
//        clients.inMemory()
//                .withClient("baidu")
//                .secret(passwordEncoder.encode("12345"))
//                .resourceIds("product_api")
//                .authorizedGrantTypes(
//                        "authorization_code",
//                        "password",
//                        "client_credentials",
//                        "implicit",
//                        "refresh_token"
//                )// 该client允许的授权类型 authorization_code,password,refresh_token,implicit,client_credentials
//                .scopes("read", "write")// 允许的授权范围
//                .autoApprove(false)
//                //加上验证回调地址
//                .redirectUris("http://www.baidu.com");

    }

    // 检测token的策略
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                // 允许form表单客户端认证,允许客户端使用client_id和client_secret获取token
                .allowFormAuthenticationForClients()
                // 通过验证返回token信息
                .checkTokenAccess("isAuthenticated()")
                // 获取token请求不进行拦截
                .tokenKeyAccess("permitAll()")
                .passwordEncoder(passwordEncoder);
    }

    // OAuth2的主配置信息
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .approvalStore(approvalStore())
                .authenticationManager(authenticationManager)
                .authorizationCodeServices(authorizationCodeServices())
                .tokenStore(tokenStore())
                .userDetailsService(userDetailsService);
    }

}
