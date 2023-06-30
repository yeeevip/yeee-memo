package vip.yeee.memo.common.appauth.client.configure;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vip.yeee.memo.common.appauth.client.constant.ApiAuthConstant;
import vip.yeee.memo.common.appauth.client.interceptor.JwtAuthCheckInterceptor;
import vip.yeee.memo.common.appauth.client.properties.ApiAuthClientProperties;

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
    private JwtAuthCheckInterceptor jwtAuthCheckInterceptor;
    @Resource
    private ApiAuthClientProperties apiAuthClientProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptor = registry.addInterceptor(jwtAuthCheckInterceptor);
        interceptor.excludePathPatterns(ApiAuthConstant.BASE_EXCLUDE_PATTERNS);
        if (CollectionUtil.isNotEmpty(apiAuthClientProperties.getExclude())) {
            interceptor.excludePathPatterns(apiAuthClientProperties.getExclude());
        }
    }

}
