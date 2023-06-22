package vip.yeee.memo.integrate.base.web.config;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vip.yeee.memo.integrate.base.web.properties.BaseWebProperty;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/6/22 18:55
 */
@Configuration
public class BaseWebMvcConfigurer implements WebMvcConfigurer {

    @Resource
    private BaseWebProperty baseWebProperty;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (baseWebProperty == null || baseWebProperty.getCors() == null || !baseWebProperty.getCors().getEnable()) {
            return;
        }
        if (CollectionUtil.isEmpty(baseWebProperty.getCors().getMappings())) {
            registry.addMapping("/**")
                    .allowedOriginPatterns("*")
                    .allowedMethods("*")
                    .allowedHeaders("*")
                    .allowCredentials(true)
                    .maxAge(3600);
        } else {
            for (String mapping : baseWebProperty.getCors().getMappings()) {
                registry.addMapping(mapping)
                        .allowedOriginPatterns("*")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        }

    }

}
