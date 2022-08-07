package vip.yeee.memo.integrate.common.security.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 网关白名单配置
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ConfigurationProperties(prefix = "secure.ignore")
public class IgnoreUrlsConfig {

    private List<String> urls;
}
