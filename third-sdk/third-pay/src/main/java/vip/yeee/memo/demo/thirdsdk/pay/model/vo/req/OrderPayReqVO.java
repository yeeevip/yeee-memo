package vip.yeee.memo.demo.thirdsdk.pay.model.vo.req;

import lombok.Data;
import vip.yeee.memo.demo.thirdsdk.pay.constant.PayConstant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 15:56
 */
@Data
public class OrderPayReqVO {

    /**
     * @see  PayConstant.PAY_WAY_CODE
     */
    @NotBlank(message = "支付方式不能为空")
    private String payway;

    @NotNull(message = "订单金额不能为空")
    private BigDecimal amount;

    @NotNull(message = "订单编号不能为空")
    private String orderCode;

    private String openId;

    private String appId;

    private String payInfo;

}
