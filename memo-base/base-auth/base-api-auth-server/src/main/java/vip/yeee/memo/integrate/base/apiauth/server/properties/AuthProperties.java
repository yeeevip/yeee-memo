package vip.yeee.memo.integrate.base.apiauth.server.properties;

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
@ConfigurationProperties(prefix = "secure.auth")
public class AuthProperties {

    private String jwtSecret;

}
