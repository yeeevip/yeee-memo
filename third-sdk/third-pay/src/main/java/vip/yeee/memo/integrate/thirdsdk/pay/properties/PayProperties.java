package vip.yeee.memo.integrate.thirdsdk.pay.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/29 9:56
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "yeee.pay")
public class PayProperties {

    private WxpayProperties wx;

    private AlipayProperties ali;

    private String notifyUrl;

    @Data
    public static class WxpayProperties {

        private String apiVersion;

        private String mchId;

        private String appId;

        private String mchKey;

        private String apiV3Key;

        private String serialNo;

        private String keyPath;

        private String privateCertPath;

        private String privateKeyPath;

    }

    @Data
    public static class AlipayProperties {

        /** pid */
        private String pid;

        /** appId */
        private String appId;

        /** privateKey */
        private String privateKey;

        /** alipayPublicKey */
        private String alipayPublicKey;

        /** 签名方式 **/
        private String signType;

        /** 是否使用证书方式 **/
        private Boolean useCert = false;

        /** app 证书 **/
        private String appPublicCert;

        /** 支付宝公钥证书（.crt格式） **/
        private String alipayPublicCert;

        /** 支付宝根证书 **/
        private String alipayRootCert;

    }

}
