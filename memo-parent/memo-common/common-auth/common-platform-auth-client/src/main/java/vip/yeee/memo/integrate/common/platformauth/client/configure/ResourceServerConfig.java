package vip.yeee.memo.integrate.common.platformauth.client.configure;

import cn.hutool.core.util.ArrayUtil;
import com.google.common.collect.Sets;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import vip.yeee.memo.integrate.base.model.annotation.AnonymousAccess;
import vip.yeee.memo.integrate.base.util.LogUtils;
import vip.yeee.memo.integrate.base.websecurityoauth2.constant.AuthConstant;
import vip.yeee.memo.integrate.common.platformauth.client.properties.AuthClientProperties;
import vip.yeee.memo.integrate.common.platformauth.client.handle.AccessDeniedHandler;
import vip.yeee.memo.integrate.common.platformauth.client.handle.AuthenticationEntryPointHandler;
import vip.yeee.memo.integrate.base.websecurityoauth2.constant.SecurityUserTypeEnum;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 资源服务器配置
 *
 * @author yeeee
 * @since 2022/4/28 17:02
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final static Logger log = LogUtils.commonAuthLog();
    @Resource
    private AuthClientProperties authClientProperties;
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private OkHttpClient okHttpClient;
    private static final Set<String> anonymousUrls = Sets.newHashSet();

    @PostConstruct
    public void init() {
        anonymousUrls.addAll(authClientProperties.getExclude());
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
        handlerMethods.forEach((k, v) -> {
            AnonymousAccess anonymousAccess = v.getMethodAnnotation(AnonymousAccess.class);
            // spring.mvc.pathmatch.matching-strategy: ant_path_matcher
            if (k.getPatternsCondition() != null && anonymousAccess != null && anonymousAccess.valid()) {
                anonymousUrls.addAll(k.getPatternsCondition().getPatterns().stream().map(p -> p).collect(Collectors.toList()));
            }
//            if (k.getPathPatternsCondition() != null && anonymousAccess != null && anonymousAccess.valid()) {
//                anonymousUrls.addAll(k.getPathPatternsCondition().getPatterns().stream().map(PathPattern::getPatternString).collect(Collectors.toList()));
//            }
        });
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

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
                .antMatchers(ArrayUtil.toArray(anonymousUrls, String.class)).permitAll()
                // 内部调用
                .antMatchers("/server/**").permitAll()
//                .antMatchers("/**/front/**").hasAnyAuthority(SecurityUserTypeEnum.SYSTEM_USER.getRole())
//                .antMatchers("/**/system/**").hasAnyAuthority(SecurityUserTypeEnum.FRONT_USER.getRole())
                .antMatchers("/**/front/**").hasRole(SecurityUserTypeEnum.FRONT_USER.getRole())
                .antMatchers("/**/system/**").hasRole(SecurityUserTypeEnum.SYSTEM_USER.getRole())
                .anyRequest().authenticated();

        http.addFilterBefore(new AuthenticationBeforeFilter(), AbstractPreAuthenticatedProcessingFilter.class);

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
        OkHttp3ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
        RestTemplate restTemplate = new RestTemplate(factory);
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

    private static class AuthenticationBeforeFilter extends OncePerRequestFilter {

        @Override
        public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws IOException, ServletException {

            if (anonymousUrls.contains(request.getRequestURI())) {
                log.info("[YEEE认证] - 包含匿名请求，url = {}", request.getRequestURI());
                // 在此处创建HttpServletRequestWrapper对象并包装原始请求
                request = new HttpServletRequestWrapper(request) {
                    @Override
                    public String getHeader(String name) {
                        if (AuthConstant.JWT_TOKEN_HEADER.equalsIgnoreCase(name)) {
                            return null;
                        }
                        return super.getHeader(name);
                    }

                    @Override
                    public Enumeration<String> getHeaders(String name) {
                        if (AuthConstant.JWT_TOKEN_HEADER.equalsIgnoreCase(name)) {
                            return Collections.emptyEnumeration();
                        }
                        return super.getHeaders(name);
                    }

                    @Override
                    public Enumeration<String> getHeaderNames() {
                        List<String> headerNames = Collections.list(super.getHeaderNames());
                        headerNames.remove(AuthConstant.JWT_TOKEN_HEADER);
                        return Collections.enumeration(headerNames);
                    }
                };
            }
            filterChain.doFilter(request, response);
        }
    }

}
