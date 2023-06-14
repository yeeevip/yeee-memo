package vip.yeee.memo.integrate.thirdsdk.pay.paykit.wxpay.v3.partner;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.*;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayContext;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.wxpay.v3.partner.request.WxPayUnifiedOrderV3PartnerRequest;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/22 18:04
 */
@Slf4j
@Component
public class WxWpPartnerV3PayKit extends WxPartnerV3PayKit {

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.WX_WP;
    }

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            PayContext payContext = PayContext.getContext();
            WxPayService wxPayService = payContext.getWxPayService();
            WxPayConfigBO wxPayConfig = payContext.getWxPayConfig();
            WxPayUnifiedOrderV3Result response;
            WxPayUnifiedOrderV3PartnerRequest request = super.buildPartnerUnifiedOrderRequest(reqBO);
            request.setSpAppid(wxPayConfig.getAppId());
            request.setSubAppId(wxPayConfig.getSubAppId());
            WxPayUnifiedOrderV3PartnerRequest.Payer payer = new WxPayUnifiedOrderV3PartnerRequest.Payer();
            if (StrUtil.isNotBlank(wxPayConfig.getSubMiniAppId())) {
                payer.setSubOpenid(reqBO.getOpenid());
            } else {
                payer.setSpOpenid(reqBO.getOpenid());
            }
            request.setPayer(payer);
            String url = wxPayService.getPayBaseUrl() + TradeTypeEnum.JSAPI.getPartnerUrl();
            String responseStr = wxPayService.postV3(url, GSON.toJson(request));
            response = GSON.fromJson(responseStr, WxPayUnifiedOrderV3Result.class);
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
