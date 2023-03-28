package vip.yeee.memo.integrate.thirdsdk.pay.paykit.wxpay.v3.partner;

import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.service.WxPayService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.*;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayContext;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayKit;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.wxpay.v3.BaseWxV3PayKit;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.wxpay.v3.partner.request.WxPayUnifiedOrderV3PartnerRequest;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 10:52
 */
@Slf4j
public abstract class WxPartnerV3PayKit extends BaseWxV3PayKit implements PayKit {

    protected static final Gson GSON = new GsonBuilder().create();

    @Override
    public abstract UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO);

    @Override
    public ChannelRetMsgBO queryOrder(QueryOrderReqBO reqBO) {
        try {
            PayContext payContext = PayContext.getContext();
            WxPayService wxPayService = payContext.getWxPayService();
            WxPayConfigBO wxPayConfig = payContext.getWxPayConfig();
            String url = String.format("/v3/pay/partner/transactions/out-trade-no/%s?sp_mchid=%s&sub_mchid=%s"
                    , reqBO.getOrderCode(), wxPayConfig.getMchId(), wxPayConfig.getSubMchId());
            String response = wxPayService.getV3(wxPayService.getPayBaseUrl() + url);
            WxPayOrderQueryV3Result result = GSON.fromJson(response, WxPayOrderQueryV3Result.class);
            String channelState = result.getTradeState();
            if ("SUCCESS".equals(channelState)) {
                return ChannelRetMsgBO.confirmSuccess(result.getTransactionId());
            } else if ("USERPAYING".equals(channelState)) { //支付中，等待用户输入密码
                return ChannelRetMsgBO.waiting(); //支付中
            } else if ("CLOSED".equals(channelState)
                    || "REVOKED".equals(channelState)
                    || "PAYERROR".equals(channelState)) {  //CLOSED—已关闭， REVOKED—已撤销(刷卡支付), PAYERROR--支付失败(其他原因，如银行返回失败)
                return ChannelRetMsgBO.confirmFail(); //支付失败
            }else{
                return ChannelRetMsgBO.unknown();
            }
        } catch (Exception e) {
            log.info("【微信V3支付-查询订单】- 失败 reqBO = {}", reqBO, e);
            throw new BizException(e.getMessage());
        }
    }

    protected WxPayUnifiedOrderV3PartnerRequest buildPartnerUnifiedOrderRequest(UnifiedOrderReqBO reqBO) {
        WxPayConfigBO wxPayConfig = PayContext.getContext().getWxPayConfig();
        // 微信统一下单请求对象
        WxpayUnifiedOrderReqBO wxReqBO = (WxpayUnifiedOrderReqBO)reqBO;
        WxPayUnifiedOrderV3PartnerRequest request = new WxPayUnifiedOrderV3PartnerRequest();
        request.setSpMchId(wxPayConfig.getMchId());
        request.setSpAppid(wxPayConfig.getAppId());
        request.setSubMchId(wxPayConfig.getSubMchId());
        request.setSubAppId(wxPayConfig.getSubAppId());
        request.setNotifyUrl(getPayNotifyUrl());
        request.setOutTradeNo(wxReqBO.getOrderCode());
        request.setDescription(wxReqBO.getOrderDesc());
        //构建金额信息
        WxPayUnifiedOrderV3PartnerRequest.Amount amount = new WxPayUnifiedOrderV3PartnerRequest.Amount();
        //设置币种信息
        amount.setCurrency("CNY");
        //设置金额
        amount.setTotal(wxReqBO.getPayMoney());
        request.setAmount(amount);
        return request;
    }

}
