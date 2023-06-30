package vip.yeee.memo.common.appauth.server.properties;

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
@ConfigurationProperties(prefix = "yeee.apiauth.jwt")
public class ApiAuthServerProperties {

    private Boolean userJws = false;

    private String secret;

    private Integer expire = 3600;

}
