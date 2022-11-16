package vip.yeee.memo.integrate.base.webauth.client.configure;

import cn.hutool.core.util.ArrayUtil;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;
import vip.yeee.memo.integrate.base.webauth.client.properties.AuthClientProperties;
import vip.yeee.memo.integrate.common.websecurity.annotation.AnonymousAccess;
import vip.yeee.memo.integrate.base.webauth.client.handle.AccessDeniedHandler;
import vip.yeee.memo.integrate.base.webauth.client.handle.AuthenticationEntryPointHandler;
import vip.yeee.memo.integrate.common.websecurity.constant.SecurityUserTypeEnum;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 资源服务器配置
 *
 * @author yeeee
 * @since 2022/4/28 17:02
 */
@Slf4j
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Resource
    private AuthClientProperties authClientProperties;
    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        Set<String> anonymousUrls = Sets.newHashSet();
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
        handlerMethods.forEach((k, v) -> {
            AnonymousAccess anonymousAccess = v.getMethodAnnotation(AnonymousAccess.class);
            if (k.getPathPatternsCondition() != null && anonymousAccess != null && anonymousAccess.valid()) {
                anonymousUrls.addAll(k.getPathPatternsCondition().getPatterns().stream().map(PathPattern::getPatternString).collect(Collectors.toList()));
            }
        });

        http.csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .exceptionHandling()
                // 处理未认证
                .authenticationEntryPoint(authenticationEntryPoint())
                // 处理未授权
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                // 不创建会话
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .antMatchers(ArrayUtil.toArray(authClientProperties.getExclude(), String.class)).permitAll()
                .antMatchers(ArrayUtil.toArray(anonymousUrls, String.class)).permitAll()
//                .antMatchers("/**/front/**").hasAnyAuthority(SecurityUserTypeEnum.SYSTEM_USER.getRole())
//                .antMatchers("/**/system/**").hasAnyAuthority(SecurityUserTypeEnum.FRONT_USER.getRole())
                .antMatchers("/**/front/**").hasRole(SecurityUserTypeEnum.FRONT_USER.getRole())
                .antMatchers("/**/system/**").hasRole(SecurityUserTypeEnum.SYSTEM_USER.getRole())
                .anyRequest().authenticated();

    }


    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPointHandler();
    }

    @Bean("remoteTokenLbRestTemplate")
    @LoadBalanced
    public RestTemplate remoteTokenLbRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401){
                    super.handleError(response);
                }
            }
        });
        return restTemplate;
    }

    @Bean
    @Primary
    public ResourceServerTokenServices resourceServerTokenServices(ResourceServerProperties resourceServerProperties){
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setRestTemplate(remoteTokenLbRestTemplate());
        remoteTokenServices.setCheckTokenEndpointUrl(resourceServerProperties.getTokenInfoUri());
        remoteTokenServices.setClientId(resourceServerProperties.getClientId());
        remoteTokenServices.setClientSecret(resourceServerProperties.getClientSecret());
        return remoteTokenServices;
    }

}
