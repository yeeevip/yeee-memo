package vip.yeee.memo.integrate.common.platformauth.client.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vip.yeee.memo.integrate.common.platformauth.client.interceptor.SecurityTokenInterceptor;

import javax.annotation.Resource;

/**
 * 资源配置类
 *
 * @author yeeee
 * @since 2022/7/12 14:16
 */
@Configuration
public class ResourceServerWebMvcInterceptorConfig implements WebMvcConfigurer {

    @Resource
    private SecurityTokenInterceptor securityTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityTokenInterceptor).addPathPatterns("/**");
    }

}
