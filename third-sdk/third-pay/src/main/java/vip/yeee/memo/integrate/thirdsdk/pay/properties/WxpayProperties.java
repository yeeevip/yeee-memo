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
@ConfigurationProperties(prefix = "yeee.pay.wx")
public class WxpayProperties {

    private String apiVersion;

    private String mchId;

    private String appId;

    private String mchKey;

    private String apiV3Key;

    private String serialNo;

    private String keyPath;

    private String privateCertPath;

    private String privateKeyPath;

    private String notifyUrl;

}
