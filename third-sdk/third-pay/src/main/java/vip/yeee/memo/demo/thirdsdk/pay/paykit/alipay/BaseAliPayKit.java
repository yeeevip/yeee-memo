package vip.yeee.memo.demo.thirdsdk.pay.paykit.alipay;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import lombok.extern.slf4j.Slf4j;
import vip.yeee.memo.base.model.exception.BizException;
import vip.yeee.memo.base.web.utils.SpringContextUtils;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.*;
import vip.yeee.memo.demo.thirdsdk.pay.paykit.PayContext;
import vip.yeee.memo.demo.thirdsdk.pay.paykit.PayKit;
import vip.yeee.memo.demo.thirdsdk.pay.properties.PayProperties;
import vip.yeee.memo.demo.thirdsdk.pay.utils.AmountUtil;

import java.util.Map;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/26 10:53
 */
@Slf4j
public abstract class BaseAliPayKit implements PayKit {

    @Override
    public abstract UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO);

    @Override
    public ChannelRetMsgBO queryOrder(QueryOrderReqBO reqBO) {
        AliPayConfigBO aliPayConfig = PayContext.getContext().getAliPayConfig();
        AlipayTradeQueryRequest req = new AlipayTradeQueryRequest();
        if (StrUtil.isNotBlank(aliPayConfig.getAuthToken())) {
            req.putOtherTextParam("app_auth_token", aliPayConfig.getAuthToken());
        }
        // 商户订单号，商户网站订单系统中唯一订单号，必填
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(reqBO.getOrderCode());
        req.setBizModel(model);

        AlipayTradeQueryResponse alipayResp;
        try {
            if (aliPayConfig.getUseCert()) {
                alipayResp = PayContext.getContext().getAlipayClient().certificateExecute(req);
            } else {
                alipayResp = PayContext.getContext().getAlipayClient().execute(req);
            }
        } catch (AlipayApiException e) {
            throw new BizException(e.getMessage());
        }
        String result = alipayResp.getTradeStatus();

        if("TRADE_SUCCESS".equals(result)) {
            return ChannelRetMsgBO.confirmSuccess(alipayResp.getTradeNo());  // 支付成功
        }else if("WAIT_BUYER_PAY".equals(result)) {
            return ChannelRetMsgBO.waiting(); // 支付中
        }
        return ChannelRetMsgBO.waiting(); // 支付中
    }

    @Override
    public ChannelRetMsgBO closeOrder(CloseOrderReqBO reqBO) {
        AlipayTradeCloseRequest req = new AlipayTradeCloseRequest();
        // 商户订单号，商户网站订单系统中唯一订单号，必填
        AlipayTradeCloseModel model = new AlipayTradeCloseModel();
        model.setOutTradeNo(reqBO.getOrderCode());
        req.setBizModel(model);
        AlipayTradeCloseResponse alipayResp;
        try {
            alipayResp = PayContext.getContext().getAlipayClient().execute(req);
        } catch (AlipayApiException e) {
            throw new BizException(e.getMessage());
        }
        // 返回状态成功
        if (alipayResp.isSuccess()) {
            return ChannelRetMsgBO.confirmSuccess(alipayResp.getTradeNo());
        }else {
            return ChannelRetMsgBO.sysError(alipayResp.getSubMsg());
        }
    }

    @Override
    public ChannelRetMsgBO refundOrder(RefundOrderReqBO reqBO) {
        AliPayConfigBO aliPayConfig = PayContext.getContext().getAliPayConfig();
        AlipayTradeRefundRequest req = new AlipayTradeRefundRequest();
        if (StrUtil.isNotBlank(aliPayConfig.getAuthToken())) {
            req.putOtherTextParam("app_auth_token", aliPayConfig.getAuthToken());
        }
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
//        model.setOutTradeNo(reqBO.getPayOrderCode());
        model.setTradeNo(reqBO.getOutPayOrderId());
        model.setOutRequestNo(reqBO.getRefundOrderCode());
        model.setRefundAmount(AmountUtil.convertCent2Dollar(reqBO.getRefundAmount().toString()));
        model.setRefundReason(reqBO.getRefundReason());
        req.setBizModel(model);
        req.setNotifyUrl(getRefundNotifyUrl());
        AlipayTradeRefundResponse alipayResp;
        try {
            if (aliPayConfig.getUseCert()) {
                alipayResp = PayContext.getContext().getAlipayClient().certificateExecute(req);
            } else {
                alipayResp = PayContext.getContext().getAlipayClient().execute(req);
            }
        } catch (AlipayApiException e) {
            throw new BizException(e.getMessage());
        }
        ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
        retMsgBO.setChannelAttach(alipayResp.getBody());
        if (alipayResp.isSuccess()) {
            retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS);
            retMsgBO.setChannelOrderId(alipayResp.getTradeNo());
        } else {
            retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_FAIL);
            retMsgBO.setChannelErrCode(alipayResp.getSubCode());
            retMsgBO.setChannelErrMsg(alipayResp.getSubMsg());
        }
        return retMsgBO;
    }

    @Override
    public ChannelRetMsgBO transfer(TransferReqBO reqBO) {
        AlipayFundTransUniTransferRequest req = new AlipayFundTransUniTransferRequest();
        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
        model.setTransAmount(AmountUtil.convertCent2Dollar(reqBO.getAmount())); //转账金额，单位：元。
        model.setOutBizNo(reqBO.getTransferId()); //商户转账唯一订单号
        model.setRemark(reqBO.getTransferDesc()); //转账备注
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");   // 销售产品码。单笔无密转账固定为 TRANS_ACCOUNT_NO_PWD
        model.setBizScene("DIRECT_TRANSFER");           // 业务场景 单笔无密转账固定为 DIRECT_TRANSFER。
        model.setOrderTitle("转账");                     // 转账业务的标题，用于在支付宝用户的账单里显示。
        model.setBusinessParams(reqBO.getChannelExtra());   // 转账业务请求的扩展参数 {\"payer_show_name_use_alias\":\"xx公司\"}

        Participant accPayeeInfo = new Participant();
        accPayeeInfo.setName(reqBO.getAccountName()); //收款方真实姓名
        accPayeeInfo.setIdentityType("ALIPAY_LOGON_ID");    //ALIPAY_USERID： 支付宝用户ID      ALIPAY_LOGONID:支付宝登录账号
        accPayeeInfo.setIdentity(reqBO.getAccountNo()); //收款方账户
        model.setPayeeInfo(accPayeeInfo);

        req.setBizModel(model);

        AlipayFundTransUniTransferResponse alipayResp;
        try {
            alipayResp = PayContext.getContext().getAlipayClient().execute(req);
        } catch (AlipayApiException e) {
            throw new BizException(e.getMessage());
        }
        ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
        if (alipayResp.isSuccess()) {
            retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS);
            retMsgBO.setChannelOrderId(alipayResp.getOrderId());
        } else {
            //若 系统繁忙， 无法确认结果
            if("SYSTEM_ERROR".equalsIgnoreCase(alipayResp.getSubCode())) {
                return ChannelRetMsgBO.waiting();
            }
            retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_FAIL);
            retMsgBO.setChannelErrCode(alipayResp.getSubCode());
            retMsgBO.setChannelErrMsg(alipayResp.getSubMsg());
        }
        return retMsgBO;
    }

    @Override
    public Pair<String, ChannelRetMsgBO> checkAndParsePayNoticeParams() throws Exception {
        Map<String, String> map = ServletUtil.getParamMap(SpringContextUtils.getHttpServletRequest());
        log.info("checkAndParseNoticeParams map = {}", map);
        AliPayConfigBO ali = PayContext.getContext().getAliPayConfig();
        boolean verifyResult;
        if (ali.getUseCert()) {  //证书方式
            verifyResult = AlipaySignature.rsaCertCheckV1(map, ali.getAlipayPublicCert(), "utf-8", ali.getSignType());
        } else {
            verifyResult = AlipaySignature.rsaCheckV1(map, ali.getAlipayPublicCert(), "utf-8", ali.getSignType());
        }
        if (!verifyResult) {
            throw new Exception("验签失败");
        }

        ChannelRetMsgBO result = new ChannelRetMsgBO();
        result.setChannelOrderId(map.get("trade_no")); //渠道订单号
        result.setChannelUserId(map.get("buyer_id")); //支付用户ID
        result.setResponseEntity(PayKit.getAliSuccessResp("SUCCESS")); //响应数据

        result.setChannelState(ChannelRetMsgBO.ChannelState.WAITING); // 默认支付中

        //验签成功后判断上游订单状态
        if("TRADE_SUCCESS".equals(map.get("trade_status")) || "TRADE_FINISHED".equals(map.get("trade_status"))) {
            result.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS);
        }else if("TRADE_CLOSED".equals(map.get("trade_status"))){
            result.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_FAIL);
        }
        return Pair.of(map.get("out_trade_no"), result);
    }

    @Override
    public Pair<String, ChannelRetMsgBO> checkAndParseRefundNoticeParams() throws Exception {
        return checkAndParsePayNoticeParams();
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
