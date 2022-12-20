package vip.yeee.memo.integrate.thirdsdk.pay.paykit.wxpay.v3;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.github.binarywang.wxpay.bean.entpay.EntPayRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.*;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.v3.auth.AutoUpdateCertificatesVerifier;
import com.github.binarywang.wxpay.v3.auth.PrivateKeySigner;
import com.github.binarywang.wxpay.v3.auth.WxPayCredentials;
import com.github.binarywang.wxpay.v3.util.PemUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.*;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayKit;
import vip.yeee.memo.integrate.thirdsdk.pay.properties.PayProperties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 10:52
 */
@Slf4j
public abstract class AbstractWxV3PayKit implements PayKit {

    @Resource
    protected WxPayService wxPayService;
    @Resource
    protected PayProperties payProperties;

    @Override
    public abstract UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO);

    @Override
    public ChannelRetMsgBO queryOrder(QueryOrderReqBO reqBO) {
        try {
            WxPayOrderQueryV3Request req = new WxPayOrderQueryV3Request();
            req.setOutTradeNo(reqBO.getOrderCode());
            WxPayOrderQueryV3Result result = wxPayService.queryOrderV3(req);
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

    @Override
    public ChannelRetMsgBO closeOrder(CloseOrderReqBO reqBO) {
        try {
            WxPayOrderCloseV3Request req = new WxPayOrderCloseV3Request();
            req.setOutTradeNo(reqBO.getOrderCode());
            wxPayService.closeOrderV3(req);
            return ChannelRetMsgBO.confirmSuccess(null);
        } catch (Exception e) {
            log.info("【微信V3支付-关闭订单】- 失败 reqBO = {}", reqBO, e);
            throw new BizException(e.getMessage());
        }
    }

    @Override
    public ChannelRetMsgBO refundOrder(RefundOrderReqBO reqBO) {
        try {
            WxPayRefundV3Request req = new WxPayRefundV3Request();
            req.setOutTradeNo(reqBO.getPayOrderCode());    // 商户订单号
            req.setOutRefundNo(reqBO.getRefundOrderId()); // 退款单号
            req.setNotifyUrl(getRefundNotifyUrl(reqBO.getPayOrderCode()));   // 回调url
            WxPayRefundV3Request.Amount amount = new WxPayRefundV3Request.Amount();
            amount.setTotal(reqBO.getAmount().intValue());   // 订单总金额
            amount.setRefund(reqBO.getRefundAmount().intValue()); // 退款金额
            amount.setCurrency("CNY");
            req.setAmount(amount);
            WxPayRefundV3Result result = wxPayService.refundV3(req);
            ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
            if ("SUCCESS".equals(result.getStatus())) { // 退款成功
                retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS);
                retMsgBO.setChannelOrderId(result.getRefundId());
            } else if ("PROCESSING".equals(result.getStatus())) { // 退款处理中
                retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.WAITING);
                retMsgBO.setChannelOrderId(result.getRefundId());
            }else{
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
    public ChannelRetMsgBO transfer(TransferReqBO reqBO) {
        try {
            EntPayRequest request = new EntPayRequest();
            request.setMchAppid(payProperties.getWx().getAppId());  // 商户账号appid
            request.setMchId(payProperties.getWx().getMchId());  //商户号
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
            EntPayResult entPayResult = wxPayService.getEntPayService().entPay(request);
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

    protected WxPayUnifiedOrderV3Request buildUnifiedOrderRequest(UnifiedOrderReqBO reqBO) {
        // 微信统一下单请求对象
        WxpayUnifiedOrderReqBO wxReqBO = (WxpayUnifiedOrderReqBO)reqBO;
        WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request();
        request.setOutTradeNo(wxReqBO.getOrderCode());
        request.setDescription(wxReqBO.getOrderDesc());
        WxPayUnifiedOrderV3Request.Payer payer = new WxPayUnifiedOrderV3Request.Payer();
        payer.setOpenid("openid");
        request.setPayer(payer);
        //构建金额信息
        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        //设置币种信息
        amount.setCurrency("CNY");
        //设置金额
        amount.setTotal(wxReqBO.getPayMoney());
        request.setAmount(amount);
        return request;
    }

    @Override
    public Pair<String, ChannelRetMsgBO> checkAndParseNoticeParams(HttpServletRequest request) throws Exception {

        SignatureHeader header = new SignatureHeader();
        header.setTimeStamp(request.getHeader("Wechatpay-Timestamp"));
        header.setNonce(request.getHeader("Wechatpay-Nonce"));
        header.setSerial(request.getHeader("Wechatpay-Serial"));
        header.setSignature(request.getHeader("Wechatpay-Signature"));

        // 获取加密信息
        String params = ServletUtil.getBody(request);

        log.info("\n【请求头信息】：{}\n【加密数据】：{}", header, params);

        WxPayConfig wxPayConfig = wxPayService.getConfig();
        // 自动获取微信平台证书
        PrivateKey privateKey = PemUtils.loadPrivateKey(new FileInputStream(wxPayConfig.getPrivateKeyPath()));
        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                new WxPayCredentials(wxPayConfig.getMchId(), new PrivateKeySigner(wxPayConfig.getCertSerialNo(), privateKey)),
                wxPayConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8));
        wxPayConfig.setVerifier(verifier);
        wxPayService.setConfig(wxPayConfig);

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
        channelResult.setResponseEntity(PayKit.getWxV3SuccessResp()); //响应数据

        return Pair.of(result.getOutTradeNo(), channelResult);
    }

    protected String getPayNotifyUrl(String orderId) {
        return String.format(payProperties.getNotifyUrl(), getPayway().toLowerCase() + "V3", orderId);
    }

    protected String getRefundNotifyUrl(String orderId) {
        return String.format(payProperties.getRefundNotifyUrl(), getPayway().toLowerCase() + "V3", orderId);
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
