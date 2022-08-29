package vip.yeee.memo.integrate.thirdsdk.pay.service;

import cn.hutool.core.lang.Pair;
import vip.yeee.memo.integrate.thirdsdk.pay.model.notice.ChannelRetMsg;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.UnifiedOrderReqBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.UnifiedOrderRespBO;

import javax.servlet.http.HttpServletRequest;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 16:42
 */
public interface PayChannelService {

    UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO);

    Pair<String, ChannelRetMsg> parseNoticeParamsAndCheck(HttpServletRequest request) throws Exception;

}
