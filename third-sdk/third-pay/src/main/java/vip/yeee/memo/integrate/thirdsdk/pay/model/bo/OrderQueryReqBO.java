package vip.yeee.memo.integrate.thirdsdk.pay.model.bo;

import lombok.Data;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/21 15:20
 */
@Data
public class OrderQueryReqBO {

    private String lesseeId;
    private String orderCode;
    private String payway;
}
