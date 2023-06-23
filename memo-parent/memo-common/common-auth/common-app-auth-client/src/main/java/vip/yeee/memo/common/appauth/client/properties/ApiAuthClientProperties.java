package vip.yeee.memo.common.appauth.client.properties;

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
public class ApiAuthClientProperties {

    private String secret;

    private Integer expire = 3600;

    private String secretUrl;

}
