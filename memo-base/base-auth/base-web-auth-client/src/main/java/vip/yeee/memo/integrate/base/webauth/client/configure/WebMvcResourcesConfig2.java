package vip.yeee.memo.integrate.base.webauth.client.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vip.yeee.memo.integrate.base.webauth.client.interceptor.SecurityTokenInterceptor;

import javax.annotation.Resource;

/**
 * 资源配置类
 *
 * @author yeeee
 * @since 2022/7/12 14:16
 */
@Configuration
public class WebMvcResourcesConfig2 implements WebMvcConfigurer {

    @Resource
    private SecurityTokenInterceptor securityTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityTokenInterceptor).addPathPatterns("/general/**", "/front/**", "/system/**", "/admin/**", "/user/**", "/resources/**");
    }

}
