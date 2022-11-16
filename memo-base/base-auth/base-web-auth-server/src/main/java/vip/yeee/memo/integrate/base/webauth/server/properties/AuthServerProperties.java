package vip.yeee.memo.integrate.base.webauth.server.properties;

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
@ConfigurationProperties(prefix = "auth.server")
public class AuthServerProperties {

    private String jwtSecret;

}
