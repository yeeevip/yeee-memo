package vip.yeee.memo.integrate.thirdsdk.pay.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/20 17:22
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "yeee.pay.ali")
public class AlipayProperties {

    private String paySiteUrl;

}
