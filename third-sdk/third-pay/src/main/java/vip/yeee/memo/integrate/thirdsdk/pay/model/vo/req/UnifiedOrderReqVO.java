package vip.yeee.memo.integrate.thirdsdk.pay.model.vo.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 15:56
 */
@Data
public class UnifiedOrderReqVO {

    /**
     * @see  vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant.PAY_WAY_CODE
     */
    @NotBlank(message = "支付方式不能为空")
    private String payWay;

    // 订单金额，单位-分
    @NotNull(message = "订单金额不能为空")
    private Long amount;

    @NotNull(message = "订单编号不能为空")
    private String orderCode;

    @NotNull(message = "订单描述不能为空")
    private String orderDesc;

    private String authCode;

    private String payDataType;

    private String orderSubject;

    private LocalDateTime expireTime;

}
