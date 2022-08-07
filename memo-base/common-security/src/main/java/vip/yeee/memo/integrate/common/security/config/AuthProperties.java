package vip.yeee.memo.integrate.common.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 授权配置
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "user.auth")
public class AuthProperties {

    private String uri;

    private String clientId;

    private String clientSecret;

    private String grantType;
}
