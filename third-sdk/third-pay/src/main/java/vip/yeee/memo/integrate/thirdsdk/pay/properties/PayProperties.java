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

    private String siteUrl;

    private String notifyUrl;

    private String refundNotifyUrl;

    private String returnUrl;

//    private Wx wx;

//    private Ali ali;

    @Data
    public static class Wx {

        /**
         * 服务商公众号id
         */
        private String appId;
        /**
         * 服务商小程序id
         */
        private String miniAppId;
        /**
         * 服务商商户号.
         */
        private String mchId;
        /**
         * 服务商商户密钥V2.
         */
        private String mchKey;
        /**
         * 签名方式.
         * 有两种HMAC_SHA256 和MD5
         *
         * @see com.github.binarywang.wxpay.constant.WxPayConstants.SignType
         */
        private String signType;
        /**
         *
         */
        private String keyPath;
        /**
         * 服务商 秘钥地址
         */
        private String privateKeyPath;
        /**
         * 服务商 证书地址
         */
        private String privateCertPath;
        /**
         * 服务商 秘钥值. apiV3
         */
        private String apiV3Key;
        /**
         * 服务商 证书序列号值 apiV3
         */
        private String certSerialNo;

    }

    @Data
    public static class Ali {

        /** 服务商appId */
        private String appId;

        /** 商户appId **/
        private String mchAppId;

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

        /** 商户授权token **/
        private String authToken;

        private String gatewayUrl = "https://openapi.alipay.com/gateway.do";

    }
}
