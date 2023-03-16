package vip.yeee.memo.integrate.thirdsdk.pay.paykit.wxpay.v3;

import com.alibaba.fastjson.JSON;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.ChannelRetMsgBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.UnifiedOrderReqBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.UnifiedOrderRespBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.WxJsapiUnifiedOrderRespBO;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayContext;
import vip.yeee.memo.integrate.thirdsdk.pay.properties.WxPayConfig;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/22 18:04
 */
@Slf4j
@Component
public class WxJsapiV3PayKit extends WxV3PayKit {

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.WX_JSAPI;
    }

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            PayContext payContext = PayContext.getContext();
            WxPayService wxPayService = payContext.getWxPayService();
            WxPayConfig wxPayConfig = payContext.getWxPayConfig();
            WxPayUnifiedOrderV3Result response;
            WxPayUnifiedOrderV3Request request = super.buildUnifiedOrderRequest(reqBO);
            response = wxPayService.unifiedOrderV3(TradeTypeEnum.JSAPI, request);
            WxJsapiUnifiedOrderRespBO respBO = new WxJsapiUnifiedOrderRespBO();
            ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
            respBO.setChannelRetMsg(retMsgBO);
            respBO.setMchId(wxPayConfig.getMchId());
            Object payInfo = response.getPayInfo(TradeTypeEnum.JSAPI, wxPayConfig.getAppId(), wxPayConfig.getMchId(), wxPayService.getConfig().getPrivateKey());
            respBO.setPayInfo(JSON.toJSONString(payInfo));
            retMsgBO.setChannelAttach(respBO.getPayInfo());
            // 支付中
            retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.WAITING);
            retMsgBO.setChannelOrderId(response.getPrepayId());
            return respBO;
        } catch (Exception e) {
            log.info("【统一下单-微信JSAPI支付V3】- 下单失败", e);
            throw new BizException(e.getMessage());
        }
    }
}
