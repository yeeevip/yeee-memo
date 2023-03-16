package vip.yeee.memo.integrate.thirdsdk.pay.model.vo.req;

import lombok.Data;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/3/16 11:55
 */
@Data
public class UnifiedOrderRefundReqVO {

    /**
     * 退款订单号（支付系统生成订单号）
     */
    private String refundOrderId;

    /**
     * 支付订单号（与t_pay_order对应）
     */
    private String payOrderCode;

    private Long amount;

    private Long refundAmount;

    private String refundReason;

    private String refundNotifyUrl;

    private String payway;

}
