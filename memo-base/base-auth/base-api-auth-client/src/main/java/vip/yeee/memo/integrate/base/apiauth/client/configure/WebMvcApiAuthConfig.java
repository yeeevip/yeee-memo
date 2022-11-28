package vip.yeee.memo.integrate.base.apiauth.client.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vip.yeee.memo.integrate.base.apiauth.client.interceptor.JwtTokenInterceptor;

import javax.annotation.Resource;

/**
 * 资源配置类
 *
 * @author yeeee
 * @since 2022/7/12 14:16
 */
@Configuration
public class WebMvcApiAuthConfig implements WebMvcConfigurer {

    @Resource
    private JwtTokenInterceptor jwtTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/general/**", "/access/**");
    }

}
