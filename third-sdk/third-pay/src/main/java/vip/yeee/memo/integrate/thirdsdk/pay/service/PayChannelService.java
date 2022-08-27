package vip.yeee.memo.integrate.thirdsdk.pay.service;

import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.UnifiedOrderReqBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.UnifiedOrderRespBO;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 16:42
 */
public interface PayChannelService {

    UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO);

}
