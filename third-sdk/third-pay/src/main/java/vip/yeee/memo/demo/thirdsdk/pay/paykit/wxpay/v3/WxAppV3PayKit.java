package vip.yeee.memo.demo.thirdsdk.pay.paykit.wxpay.v3;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.base.model.exception.BizException;
import vip.yeee.memo.base.util.JacksonUtils;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.ChannelRetMsgBO;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.UnifiedOrderReqBO;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.UnifiedOrderRespBO;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.WxAppUnifiedOrderRespBO;
import vip.yeee.memo.demo.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.demo.thirdsdk.pay.paykit.PayContext;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/20 18:56
 */
@Slf4j
@Component
public class WxAppV3PayKit extends WxV3PayKit {

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.WX_APP;
    }

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            PayContext payContext = PayContext.getContext();
            WxPayUnifiedOrderV3Request request = super.buildUnifiedOrderRequest(reqBO);
            WxPayUnifiedOrderV3Result.AppResult result = payContext.getWxPayService().createOrderV3(TradeTypeEnum.APP, request);
            WxAppUnifiedOrderRespBO respBO = new WxAppUnifiedOrderRespBO();
            ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
            respBO.setChannelRetMsg(retMsgBO);
            respBO.setMchId(payContext.getWxPayConfig().getMchId());
            respBO.setPayInfo(JacksonUtils.toJsonString(result));
            retMsgBO.setChannelAttach(respBO.getPayInfo());
            // 支付中
            retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.WAITING);
            retMsgBO.setChannelOrderId(result.getPrepayId());
            return respBO;
        } catch (Exception e) {
            log.info("【统一下单-微信APP支付V3】- 下单失败", e);
            throw new BizException(e.getMessage());
        }
    }
}
