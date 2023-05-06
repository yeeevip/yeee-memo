package vip.yeee.memo.integrate.common.platformauth.client.properties;

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
@ConfigurationProperties(prefix = "yeee.auth.resource")
public class AuthClientProperties {

    private List<String> exclude = Lists.newArrayList();

}
