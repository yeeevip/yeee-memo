package vip.yeee.memo.integrate.thirdsdk.pay.paykit.wxpay.v3.partner;

import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.base.util.JacksonUtils;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.ChannelRetMsgBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.UnifiedOrderReqBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.UnifiedOrderRespBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.WxAppUnifiedOrderRespBO;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayContext;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.wxpay.v3.partner.request.WxPayUnifiedOrderV3PartnerRequest;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/20 18:56
 */
@Slf4j
@Component
public class WxAppPartnerV3PayKit extends WxPartnerV3PayKit {

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.WX_APP;
    }

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            PayContext payContext = PayContext.getContext();
            WxPayService wxPayService = payContext.getWxPayService();
            WxPayUnifiedOrderV3PartnerRequest request = super.buildPartnerUnifiedOrderRequest(reqBO);
            String url = wxPayService.getPayBaseUrl() + "/v3/pay/partner/transactions/app";
            String responseStr = wxPayService.postV3(url, GSON.toJson(request));
            WxPayUnifiedOrderV3Result.AppResult result = GSON.fromJson(responseStr, WxPayUnifiedOrderV3Result.AppResult.class);
            WxAppUnifiedOrderRespBO respBO = new WxAppUnifiedOrderRespBO();
            ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
            respBO.setChannelRetMsg(retMsgBO);
            respBO.setMchId(payContext.getWxPayConfig().getSubMchId());
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
