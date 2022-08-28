package vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 16:50
 */
@Data
public class UnifiedOrderReqBO {

    private String tradeType;

    private String orderCode;

    private String orderDesc;

    private Integer payMoney;

    private String extraInfo;

    private LocalDateTime expireTime;

}
