package vip.yeee.memo.integrate.base.swagger.config;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Swagger自定义配置
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "yeee.swagger")
public class SwaggerProperties {
    private boolean enable = false;
    /**
     * API文档生成基础路径
     */
    private String apiBasePackage;
    /**
     * 是否要启用登录认证
     */
    private boolean enableSecurity = true;
    /**
     * 文档标题
     */
    private String title;
    /**
     * 文档描述
     */
    private String description;
    /**
     * 文档版本
     */
    private String version;
    /**
     * 文档联系人姓名
     */
    private String contactName;
    /**
     * 文档联系人网址
     */
    private String contactUrl;
    /**
     * 文档联系人邮箱
     */
    private String contactEmail;
}
