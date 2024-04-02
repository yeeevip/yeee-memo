package vip.yeee.memo.demo.thirdsdk.pay.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/29 9:56
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "yeee.pay")
public class PayProperties {

    private String siteUrl;

    private String notifyUrl;

    private String refundNotifyUrl;

    private String returnUrl;

    private WxPayConfig wx;

    private AliPayConfig ali;
}
