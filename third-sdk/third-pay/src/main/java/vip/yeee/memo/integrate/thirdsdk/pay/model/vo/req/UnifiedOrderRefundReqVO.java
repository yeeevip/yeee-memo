package vip.yeee.memo.integrate.thirdsdk.pay.model.vo.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/3/16 11:55
 */
@Data
public class UnifiedOrderRefundReqVO {

    private String lesseeId;

    /**
     * 退款订单号（支付系统生成订单号）
     */
    @NotBlank
    private String refundOrderId;

    /**
     * 支付订单号（与t_pay_order对应）
     */
    @NotBlank
    private String payOrderCode;

    @NotNull
    private Long amount;

    @NotNull
    private Long refundAmount;

    private String refundReason;

    private String payway;

}
