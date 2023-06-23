package vip.yeee.memo.demo.thirdsdk.pay.model.bo;

import lombok.Data;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/3/16 17:10
 */
@Data
public class AliPayConfigBO {

    /** 服务商appId */
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

    private String gatewayUrl;



    /* ||||||||||||||||||||||||||||||||||||||||||||||| */

    /** 二级商户appId **/
    private String subAppId;
    /** 二级商户授权token **/
    private String authToken;
//    /** 商户授权刷新token **/
//    private String authRefreshToken;
//    /** token过期时间 **/
//    private Date expiresDate;
//    /** refreshToken过期时间 **/
//    private Date refreshExpiresDate;

}
