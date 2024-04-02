package vip.yeee.memo.base.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/7/14 15:47
 */
@Data
@Component
@ConfigurationProperties(prefix = "yeee.common")
public class YeeeCommonProperties {

    private String name;

    private String version;

}
