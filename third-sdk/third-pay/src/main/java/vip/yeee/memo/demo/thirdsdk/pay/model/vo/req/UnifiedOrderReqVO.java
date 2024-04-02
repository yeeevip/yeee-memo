package vip.yeee.memo.demo.thirdsdk.pay.model.vo.req;

import lombok.Data;
import vip.yeee.memo.demo.thirdsdk.pay.constant.PayConstant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/26 15:56
 */
@Data
public class UnifiedOrderReqVO {

    private String lesseeId;

    /**
     * @see  PayConstant.PAY_WAY_CODE
     */
    @NotBlank(message = "支付方式不能为空")
    private String payway;

    @NotNull(message = "订单金额不能为空")
    private BigDecimal amount;

    @NotNull(message = "订单编号不能为空")
    private String orderCode;

    @NotNull(message = "订单描述不能为空")
    private String orderDesc;

    private String authCode;

    private String payDataType;

    private String orderSubject;

    private String openid;

    private String appId;

}
