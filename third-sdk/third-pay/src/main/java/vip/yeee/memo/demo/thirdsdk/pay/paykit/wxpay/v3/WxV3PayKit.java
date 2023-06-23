package vip.yeee.memo.demo.thirdsdk.pay.paykit.wxpay.v3;

import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import lombok.extern.slf4j.Slf4j;
import vip.yeee.memo.base.model.exception.BizException;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.*;
import vip.yeee.memo.demo.thirdsdk.pay.paykit.PayContext;
import vip.yeee.memo.demo.thirdsdk.pay.paykit.PayKit;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 10:52
 */
@Slf4j
public abstract class WxV3PayKit extends BaseWxV3PayKit implements PayKit {

    @Override
    public abstract UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO);

    @Override
    public ChannelRetMsgBO queryOrder(QueryOrderReqBO reqBO) {
        try {
            WxPayOrderQueryV3Request req = new WxPayOrderQueryV3Request();
            req.setOutTradeNo(reqBO.getOrderCode());
            WxPayOrderQueryV3Result result = PayContext.getContext().getWxPayService().queryOrderV3(req);
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

    protected WxPayUnifiedOrderV3Request buildUnifiedOrderRequest(UnifiedOrderReqBO reqBO) {
        PayContext payContext = PayContext.getContext();
        WxPayConfigBO wxPayConfig = payContext.getWxPayConfig();
        // 微信统一下单请求对象
        WxpayUnifiedOrderReqBO wxReqBO = (WxpayUnifiedOrderReqBO)reqBO;
        WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request();
        request.setMchid(wxPayConfig.getMchId());
        request.setOutTradeNo(wxReqBO.getOrderCode());
        request.setDescription(wxReqBO.getOrderDesc());
        //构建金额信息
        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        //设置币种信息
        amount.setCurrency("CNY");
        //设置金额
        amount.setTotal(wxReqBO.getPayMoney());
        request.setAmount(amount);
        request.setNotifyUrl(getPayNotifyUrl());
        return request;
    }

}
