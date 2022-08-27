package vip.yeee.memo.integrate.thirdsdk.pay.model.bizOrder;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 14:45
 */
@Data
public class SubmitOrderReqVO {

    @NotNull(message = "商品ID不能为空")
    private Long subjectId;

    // 订单金额，单位-分
    @NotNull(message = "订单金额不能为空")
    private Long amount;

}
