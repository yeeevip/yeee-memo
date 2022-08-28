package vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder;

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
public class AlipayUnifiedOrderRespBO extends UnifiedOrderRespBO {

    private String payData;

}
