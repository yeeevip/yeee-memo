package vip.yeee.memo.integrate.thirdsdk.pay.properties;

import lombok.Data;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/3/16 17:10
 */
@Data
public class AliPayConfig {

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
