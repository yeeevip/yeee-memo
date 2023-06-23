package vip.yeee.memo.demo.thirdsdk.pay.paykit.wxpay.v2;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Pair;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayOrderCloseRequest;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderCloseResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import vip.yeee.memo.base.model.exception.BizException;
import vip.yeee.memo.base.web.utils.SpringContextUtils;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.*;
import vip.yeee.memo.demo.thirdsdk.pay.paykit.wxpay.v3.WxAppV3PayKit;
import vip.yeee.memo.demo.thirdsdk.pay.paykit.PayContext;
import vip.yeee.memo.demo.thirdsdk.pay.paykit.PayKit;
import vip.yeee.memo.demo.thirdsdk.pay.properties.PayProperties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 10:52
 */
@Slf4j
public abstract class BaseWxPayKit implements PayKit {

    @Resource
    private WxAppV3PayKit wxAppV3PayKit;

    @Override
    public abstract UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO);

    @Override
    public ChannelRetMsgBO queryOrder(QueryOrderReqBO reqBO) {
        try {
            WxPayOrderQueryRequest req = new WxPayOrderQueryRequest();
            req.setOutTradeNo(reqBO.getOrderCode());
            WxPayOrderQueryResult result = PayContext.getContext().getWxPayService().queryOrder(req);
            if ("SUCCESS".equals(result.getTradeState())) { //支付成功
                return ChannelRetMsgBO.confirmSuccess(result.getTransactionId());
            } else if ("USERPAYING".equals(result.getTradeState())) { //支付中，等待用户输入密码
                return ChannelRetMsgBO.waiting(); //支付中
            } else if ("CLOSED".equals(result.getTradeState())
                    || "REVOKED".equals(result.getTradeState())
                    || "PAYERROR".equals(result.getTradeState())) {  //CLOSED—已关闭， REVOKED—已撤销(刷卡支付), PAYERROR--支付失败(其他原因，如银行返回失败)
                return ChannelRetMsgBO.confirmFail(); //支付失败
            } else{
                return ChannelRetMsgBO.unknown();
            }
        } catch (Exception e) {
            log.info("【微信V2支付-查询订单】- 失败 reqBO = {}", reqBO, e);
            throw new BizException(e.getMessage());
        }
    }

    @Override
    public ChannelRetMsgBO closeOrder(CloseOrderReqBO reqBO) {
        try {
            WxPayOrderCloseRequest req = new WxPayOrderCloseRequest();
            req.setOutTradeNo(reqBO.getOrderCode());
            WxPayOrderCloseResult result = PayContext.getContext().getWxPayService().closeOrder(req);
            if ("SUCCESS".equals(result.getResultCode())) { //关闭订单成功
                return ChannelRetMsgBO.confirmSuccess(null);
            } else if ("FAIL".equals(result.getResultCode())) { //关闭订单失败
                return ChannelRetMsgBO.confirmFail(); //关闭失败
            } else {
                return ChannelRetMsgBO.waiting(); //关闭中
            }
        } catch (Exception e) {
            log.info("【微信V2支付-关闭订单】- 失败 reqBO = {}", reqBO, e);
            throw new BizException(e.getMessage());
        }
    }

    @Override
    public ChannelRetMsgBO refundOrder(RefundOrderReqBO reqBO) {
        try {
            WxPayRefundRequest req = new WxPayRefundRequest();
            req.setOutTradeNo(reqBO.getPayOrderCode());    // 商户订单号
//            req.setTransactionId(reqBO.getOutPayOrderId());
            req.setOutRefundNo(reqBO.getRefundOrderCode()); // 退款单号
            req.setTotalFee(reqBO.getAmount().intValue());   // 订单总金额
            req.setRefundFee(reqBO.getRefundAmount().intValue()); // 退款金额
            req.setNotifyUrl(getRefundNotifyUrl());   // 回调url
            WxPayRefundResult result = PayContext.getContext().getWxPayService().refundV2(req);
            ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
            if ("SUCCESS".equals(result.getResultCode())) { // 退款发起成功,结果主动查询
                retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.WAITING);
                retMsgBO.setChannelOrderId(result.getRefundId());
            } else {
                retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_FAIL);
                retMsgBO.setChannelErrCode(result.getErrCode());
                retMsgBO.setChannelErrMsg(result.getReturnMsg() + "##" + result.getErrCodeDes());
            }
            return retMsgBO;
        } catch (Exception e) {
            log.info("【微信V2支付-订单退款】- 失败 reqBO = {}", reqBO, e);
            throw new BizException(e.getMessage());
        }
    }

    @Override
    public ChannelRetMsgBO transfer(TransferReqBO reqBO) {
        return wxAppV3PayKit.transfer(reqBO);
    }

    protected WxPayUnifiedOrderRequest buildUnifiedOrderRequest(UnifiedOrderReqBO reqBO) {
        // 微信统一下单请求对象
        WxpayUnifiedOrderReqBO wxReqBO = (WxpayUnifiedOrderReqBO)reqBO;
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        request.setOutTradeNo(wxReqBO.getOrderCode());
        request.setBody(wxReqBO.getOrderSubject());
        request.setDetail(wxReqBO.getOrderDesc());
        request.setFeeType("CNY");
        request.setTotalFee(wxReqBO.getPayMoney());
        request.setSpbillCreateIp(wxReqBO.getClientIp());
        request.setProductId(System.currentTimeMillis()+"");
        request.setTimeExpire(LocalDateTimeUtil.format(wxReqBO.getExpireTime(), DatePattern.PURE_DATETIME_PATTERN));
        return request;
    }

    @Override
    public Pair<String, ChannelRetMsgBO> checkAndParsePayNoticeParams() throws Exception {
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        WxPayOrderNotifyResult result = WxPayOrderNotifyResult.fromXML(xmlResult);
        String orderCode = result.getOutTradeNo();
        log.info("解析数据为：orderCode = {}, params = {}", orderCode, result);
        // 验签
        result.checkResult(PayContext.getContext().getWxPayService(), WxPayConstants.SignType.MD5, true);
        ChannelRetMsgBO channelResult = new ChannelRetMsgBO();
        channelResult.setChannelState(ChannelRetMsgBO.ChannelState.WAITING); // 默认支付中
        channelResult.setChannelOrderId(result.getTransactionId()); //渠道订单号
        channelResult.setChannelUserId(result.getOpenid()); //支付用户ID
        channelResult.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS);
        channelResult.setResponseEntity(PayKit.getWxV2SuccessResp("OK"));
        return Pair.of(orderCode, channelResult);
    }

    @Override
    public Pair<String, ChannelRetMsgBO> checkAndParseRefundNoticeParams() throws Exception {
        PayContext payContext = PayContext.getContext();
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
        String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        WxPayRefundNotifyResult result = WxPayRefundNotifyResult.fromXML(xmlResult, payContext.getWxPayConfig().getMchKey());
        WxPayRefundNotifyResult.ReqInfo reqInfo = result.getReqInfo();
        String orderCode = reqInfo.getOutRefundNo();
        log.info("解析数据为：refundOrderCode = {}, params = {}", orderCode, result);
        reqInfo.getRefundStatus();
        // 验签
        result.checkResult(payContext.getWxPayService(), WxPayConstants.SignType.MD5, true);
        ChannelRetMsgBO channelResult = new ChannelRetMsgBO();
        channelResult.setChannelState(ChannelRetMsgBO.ChannelState.WAITING); // 默认支付中
        channelResult.setChannelOrderId(reqInfo.getRefundId()); //渠道订单号
        channelResult.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS);
        channelResult.setResponseEntity(PayKit.getWxV2SuccessResp("OK"));
        return Pair.of(orderCode, channelResult);
    }

    protected String getPayNotifyUrl() {
        PayContext payContext = PayContext.getContext();
        PayProperties payProperties = payContext.getPayProperties();
        return String.format(payProperties.getNotifyUrl(), getPayway().toLowerCase(), payContext.getLesseeId());
    }

    protected String getRefundNotifyUrl() {
        PayContext payContext = PayContext.getContext();
        PayProperties payProperties = payContext.getPayProperties();
        return String.format(payProperties.getRefundNotifyUrl(), getPayway().toLowerCase(), payContext.getLesseeId());
    }

}
