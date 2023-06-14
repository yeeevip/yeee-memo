package vip.yeee.memo.integrate.thirdsdk.pay.properties;

import lombok.Data;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/3/16 17:10
 */
@Data
public class WxPayConfig {

    private Integer partner;

    private String apiVersion;
    /**
     * 商户号mchId.
     */
    private String mchId;
    /**
     * 绑定的公众号appid.
     */
    private String appId;

    private String miniAppId;

    /**
     * apiV3 API证书秘钥值.
     */
    private String apiV3Key;
    /**
     * apiclient_key.pem证书文件的绝对路径或者以classpath:开头的类路径.
     */
    private String privateKeyPath;
    /**
     * apiclient_cert.p12证书文件的绝对路径或者以classpath:开头的类路径.
     */
    private String keyPath;
    /**
     * apiclient_cert.pem证书文件的绝对路径或者以classpath:开头的类路径.
     */
    private String privateCertPath;
    /**
     * apiV3 API证书序列号值
     */
    private String certSerialNo;

    // V2

    /**
     * 商户密钥.
     */
    private String mchKey;
    /**
     * 签名方式.
     * 有两种HMAC_SHA256 和MD5
     *
     * @see com.github.binarywang.wxpay.constant.WxPayConstants.SignType
     */
    private String signType;

}
