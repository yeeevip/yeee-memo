package vip.yeee.memo.integrate.common.security.config;

import cn.hutool.core.util.ArrayUtil;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import vip.yeee.memo.integrate.common.security.annotation.AnonymousAccess;
import vip.yeee.memo.integrate.common.security.handle.AccessDeniedHandlerHandle;
import vip.yeee.memo.integrate.common.security.handle.AuthenticationEntryPointHandle;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

/**
 * 资源服务器配置
 *
 * @author yeeee
 * @since 2022/4/28 17:02
 */
@Slf4j
@Configuration
@EnableResourceServer
@EnableConfigurationProperties({IgnoreUrlsConfig.class})
public class OauthResourceConfig extends ResourceServerConfigurerAdapter {

    @Resource
    private TokenStore tokenStore;
    @Resource
    private IgnoreUrlsConfig ignoreUrlsConfig;
    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        Set<String> anonymousUrls = Sets.newHashSet();
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
        handlerMethods.forEach((k, v) -> {
            AnonymousAccess anonymousAccess = v.getMethodAnnotation(AnonymousAccess.class);
            if (k.getPatternsCondition() != null && anonymousAccess != null && anonymousAccess.valid()) {
                anonymousUrls.addAll(k.getPatternsCondition().getPatterns());
            }
        });

        http.csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .exceptionHandling()
                // 处理未授权
                .accessDeniedHandler(accessDeniedHandlerHandle())
                // 处理未认证
                .authenticationEntryPoint(authenticationEntryPointHandle())
                .and()
                // 不创建会话
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .antMatchers(ArrayUtil.toArray(ignoreUrlsConfig.getUrls(), String.class)).permitAll()
                .antMatchers(ArrayUtil.toArray(anonymousUrls, String.class)).permitAll()
                .antMatchers("/**/front/**").hasAnyAuthority("FRONT-USER")
                .antMatchers("/**/admin/**").hasAnyAuthority("SYSTEM-USER")
//                .antMatchers("/**/front/private/*.html").hasAnyAuthority("MEMBER")
                .anyRequest().authenticated();
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(authenticationEntryPointHandle())
                .accessDeniedHandler(accessDeniedHandlerHandle())
                .tokenStore(tokenStore);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandlerHandle() {
        return new AccessDeniedHandlerHandle();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPointHandle() {
        return new AuthenticationEntryPointHandle();
    }

}
