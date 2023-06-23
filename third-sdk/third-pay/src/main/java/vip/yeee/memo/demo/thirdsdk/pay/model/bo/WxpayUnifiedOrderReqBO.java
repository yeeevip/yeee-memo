package vip.yeee.memo.demo.thirdsdk.pay.model.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 11:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WxpayUnifiedOrderReqBO extends UnifiedOrderReqBO {

    private String clientIp;

}
