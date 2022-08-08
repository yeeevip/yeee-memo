package vip.yeee.memo.integrate.common.swagger.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import javax.annotation.Resource;

/**
 * Swagger API文档相关配置
 */
@Configuration
@EnableSwagger2WebMvc
@Profile({"dev", "test"})
@EnableConfigurationProperties({SwaggerProperties.class})
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig extends BaseSwaggerConfig {

    @Resource
    private SwaggerProperties swaggerProperties;

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage(swaggerProperties.getApiBasePackage())
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .contactName(swaggerProperties.getContactName())
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}
