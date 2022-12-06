package vip.yeee.memo.integrate.common.scloud.gray.inner.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vip.yeee.memo.integrate.common.scloud.gray.inner.handle.GrayWebMvcInterceptor;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/6 17:29
 */
@Configuration
public class GrayInnerMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GrayWebMvcInterceptor())
                .addPathPatterns("/**");
    }

}
