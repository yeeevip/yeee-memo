package vip.yeee.memo.common.platformauth.server.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * TokenStore配置
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "yeee.webauth.token-store")
public class AuthTokenStoreProperties {

    private Db db;
    private Jwt jwt;
    private String jwtSecret;

    @Data
    public static class Db {
        private Boolean enabled = false;
    }

    @Data
    public static class Jwt {
        private Boolean enabled = false;
    }

    @Data
    public static class Redis {
        private Boolean enabled = false;
    }

}
