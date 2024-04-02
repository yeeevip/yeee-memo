package vip.yeee.memo.demo.thirdsdk.pay.model.bo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/26 16:50
 */
@Data
public class UnifiedOrderReqBO {

    private String tradeType;

    private String orderCode;

    private String orderSubject;

    private String orderDesc;

    private Integer payMoney;

    private String extraInfo;

    private LocalDateTime expireTime;

    private String notifyUrl;

    private String authCode;

    private String payDataType;

    // 一般为二级商户下的小程序或公众号appId
    private String appId;

    private String openid;

}
