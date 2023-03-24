package vip.yeee.memo.integrate.thirdsdk.pay.paykit.wxpay.v3;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.github.binarywang.wxpay.bean.entpay.EntPayRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.*;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.*;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayContext;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayKit;
import vip.yeee.memo.integrate.thirdsdk.pay.properties.WxPayConfig;

import javax.servlet.http.HttpServletRequest;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 10:52
 */
@Slf4j
public abstract class BaseWxV3PayKit implements PayKit {

    @Override
    public abstract UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO);

    @Override
    public abstract ChannelRetMsgBO queryOrder(QueryOrderReqBO reqBO);

    @Override
    public ChannelRetMsgBO refundOrder(RefundOrderReqBO reqBO) {
        try {
            PayContext payContext = PayContext.getContext();
            WxPayConfigBO wxPayConfig = payContext.getWxPayConfig();
            WxPayRefundV3Request req = new WxPayRefundV3Request();
            req.setSubMchid(StrUtil.emptyToDefault(wxPayConfig.getSubMchId(), wxPayConfig.getMchId()));
            req.setOutTradeNo(reqBO.getPayOrderCode());    // 商户订单号
            req.setOutRefundNo(reqBO.getRefundOrderId()); // 退款单号
            req.setNotifyUrl(getRefundNotifyUrl());   // 回调url
            WxPayRefundV3Request.Amount amount = new WxPayRefundV3Request.Amount();
            amount.setTotal(reqBO.getAmount().intValue());   // 订单总金额
            amount.setRefund(reqBO.getRefundAmount().intValue()); // 退款金额
            amount.setCurrency("CNY");
            req.setAmount(amount);
            WxPayRefundV3Result result = payContext.getWxPayService().refundV3(req);
            ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
            if ("SUCCESS".equals(result.getStatus())) { // 退款成功
                retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS);
                retMsgBO.setChannelOrderId(result.getRefundId());
            } else if ("PROCESSING".equals(result.getStatus())) { // 退款处理中
                retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.WAITING);
                retMsgBO.setChannelOrderId(result.getRefundId());
            } else {
                retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_FAIL);
                retMsgBO.setChannelErrMsg(result.getStatus());
            }
            return retMsgBO;
        } catch (Exception e) {
            log.info("【微信V3支付-订单退款】- 失败 reqBO = {}", reqBO, e);
            throw new BizException(e.getMessage());
        }
    }

    @Override
    public ChannelRetMsgBO closeOrder(CloseOrderReqBO reqBO) {
        try {
            WxPayOrderCloseV3Request req = new WxPayOrderCloseV3Request();
            req.setOutTradeNo(reqBO.getOrderCode());
            PayContext.getContext().getWxPayService().closeOrderV3(req);
            return ChannelRetMsgBO.confirmSuccess(null);
        } catch (Exception e) {
            log.info("【微信V3支付-关闭订单】- 失败 reqBO = {}", reqBO, e);
            throw new BizException(e.getMessage());
        }
    }

    @Override
    public ChannelRetMsgBO transfer(TransferReqBO reqBO) {
        try {
            WxPayConfigBO wxPayConfig = PayContext.getContext().getWxPayConfig();
            EntPayRequest request = new EntPayRequest();
            request.setMchAppid(wxPayConfig.getAppId());  // 商户账号appid
            request.setMchId(wxPayConfig.getMchId());  //商户号
            request.setPartnerTradeNo(reqBO.getTransferId()); //商户订单号
            request.setOpenid(reqBO.getAccountNo()); //openid
            request.setAmount(reqBO.getAmount().intValue()); //付款金额，单位为分
            request.setSpbillCreateIp(reqBO.getClientIp());
            request.setDescription(reqBO.getTransferDesc()); //付款备注
            if (StrUtil.isNotEmpty(reqBO.getAccountName())) {
                request.setReUserName(reqBO.getAccountName());
                request.setCheckName("FORCE_CHECK");
            } else {
                request.setCheckName("NO_CHECK");
            }
            EntPayResult entPayResult = PayContext.getContext().getWxPayService().getEntPayService().entPay(request);
            // SUCCESS/FAIL，注意：当状态为FAIL时，存在业务结果未明确的情况。如果状态为FAIL，请务必关注错误代码（err_code字段），通过查询接口确认此次付款的结果。
            if ("SUCCESS".equalsIgnoreCase(entPayResult.getResultCode())) {
                return ChannelRetMsgBO.confirmSuccess(entPayResult.getPaymentNo());
            }else{
                return ChannelRetMsgBO.waiting();
            }
        } catch (Exception e) {
            log.info("【微信V3支付-企业转账】- 失败 reqBO = {}", reqBO, e);
            throw new BizException(e.getMessage());
        }
    }

    @Override
    public Pair<String, ChannelRetMsgBO> checkAndParsePayNoticeParams(HttpServletRequest request) throws Exception {

        SignatureHeader header = new SignatureHeader();
        header.setTimeStamp(request.getHeader("Wechatpay-Timestamp"));
        header.setNonce(request.getHeader("Wechatpay-Nonce"));
        header.setSerial(request.getHeader("Wechatpay-Serial"));
        header.setSignature(request.getHeader("Wechatpay-Signature"));

        // 获取加密信息
        String params = ServletUtil.getBody(request);

        log.info("\n【请求头信息】：{}\n【加密数据】：{}", header, params);

        WxPayService wxPayService = PayContext.getContext().getWxPayService();

        WxPayOrderNotifyV3Result.DecryptNotifyResult result = wxPayService.parseOrderNotifyV3Result(params, header).getResult();
        log.info("解析数据为：orderCode = {}, params = {}", result.getOutTradeNo(), result);

        ChannelRetMsgBO channelResult = new ChannelRetMsgBO();
        channelResult.setChannelState(ChannelRetMsgBO.ChannelState.WAITING); // 默认支付中

        String channelState = result.getTradeState();
        if ("SUCCESS".equals(channelState)) {
            channelResult.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS);
        }else if("CLOSED".equals(channelState)
                || "REVOKED".equals(channelState)
                || "PAYERROR".equals(channelState)){  //CLOSED—已关闭， REVOKED—已撤销, PAYERROR--支付失败
            channelResult.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_FAIL); //支付失败
        }
        channelResult.setChannelOrderId(result.getTransactionId()); //渠道订单号
        WxPayOrderNotifyV3Result.Payer payer = result.getPayer();
        if (payer != null) {
            channelResult.setChannelUserId(payer.getOpenid()); //支付用户ID
        }
        channelResult.setResponseEntity(PayKit.getWxV3SuccessResp("SUCCESS")); //响应数据

        return Pair.of(result.getOutTradeNo(), channelResult);
    }

    @Override
    public Pair<String, ChannelRetMsgBO> checkAndParseRefundNoticeParams(HttpServletRequest request) throws Exception {

        SignatureHeader header = new SignatureHeader();
        header.setTimeStamp(request.getHeader("Wechatpay-Timestamp"));
        header.setNonce(request.getHeader("Wechatpay-Nonce"));
        header.setSerial(request.getHeader("Wechatpay-Serial"));
        header.setSignature(request.getHeader("Wechatpay-Signature"));

        // 获取加密信息
        String params = ServletUtil.getBody(request);

        log.info("\n【请求头信息】：{}\n【加密数据】：{}", header, params);

        WxPayService wxPayService = PayContext.getContext().getWxPayService();

        WxPayRefundNotifyV3Result.DecryptNotifyResult result = wxPayService.parseRefundNotifyV3Result(params, header).getResult();
        log.info("解析数据为：orderCode = {}, params = {}", result.getOutTradeNo(), result);

        ChannelRetMsgBO channelResult = new ChannelRetMsgBO();
        channelResult.setChannelState(ChannelRetMsgBO.ChannelState.WAITING); // 默认支付中

        String channelState = result.getRefundStatus();
        if ("SUCCESS".equals(channelState)) {
            channelResult.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS);
        } else if("CLOSED".equals(channelState)
                || "REVOKED".equals(channelState)
                || "PAYERROR".equals(channelState)){  //CLOSED—已关闭， REVOKED—已撤销, PAYERROR--支付失败
            channelResult.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_FAIL); //支付失败
        }
        channelResult.setChannelOrderId(result.getTransactionId());
        channelResult.setResponseEntity(PayKit.getWxV3SuccessResp("SUCCESS")); //响应数据

        return Pair.of(result.getOutTradeNo(), channelResult);
    }

    protected String getPayNotifyUrl() {
        PayContext payContext = PayContext.getContext();
        return String.format(payContext.getPayProperties().getNotifyUrl(), getPayway().toLowerCase(), payContext.getLesseeId());
    }

    protected String getRefundNotifyUrl() {
        PayContext payContext = PayContext.getContext();
        return String.format(payContext.getPayProperties().getRefundNotifyUrl(), getPayway().toLowerCase(), payContext.getLesseeId());
    }

    protected void commonSetErrInfo(ChannelRetMsgBO channelRetMsg, WxPayException wxPayException){
        channelRetMsg.setChannelErrCode(StrUtil.emptyToDefault(wxPayException.getReturnCode(), wxPayException.getErrCode()));
        channelRetMsg.setChannelErrMsg(wxPayException.getReturnMsg() + "##" + wxPayException.getErrCodeDes());

        // 如果仍然为空
        if(StringUtils.isEmpty(channelRetMsg.getChannelErrMsg())){
            channelRetMsg.setChannelErrMsg(StrUtil.emptyToDefault(wxPayException.getCustomErrorMsg(), wxPayException.getMessage()));
        }
    }

}
