package vip.yeee.memo.common.appauth.client.properties;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 授权配置
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "yeee.apiauth.jwt")
public class ApiAuthClientProperties {

    private String secret;

    private String secretUrl;

    private String failUrl;

    private List<String> exclude = Lists.newArrayList();

}
