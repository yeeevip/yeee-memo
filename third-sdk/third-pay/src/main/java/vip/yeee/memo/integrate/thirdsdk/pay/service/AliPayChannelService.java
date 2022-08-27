package vip.yeee.memo.integrate.thirdsdk.pay.service;

import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.UnifiedOrderReqBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.UnifiedOrderRespBO;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 10:53
 */
@Component
public class AliPayChannelService implements PayChannelService {

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        return null;
    }

}
