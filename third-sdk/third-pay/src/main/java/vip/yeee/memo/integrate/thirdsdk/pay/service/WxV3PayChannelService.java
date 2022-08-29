package vip.yeee.memo.integrate.thirdsdk.pay.service;

import cn.hutool.core.lang.Pair;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.v3.auth.AutoUpdateCertificatesVerifier;
import com.github.binarywang.wxpay.v3.auth.PrivateKeySigner;
import com.github.binarywang.wxpay.v3.auth.WxPayCredentials;
import com.github.binarywang.wxpay.v3.util.PemUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.common.model.exception.BizException;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.model.notice.ChannelRetMsg;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.*;
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
@Component
public class WxV3PayChannelService implements PayChannelService {

    @Resource
    private WxPayService wxPayService;
    @Resource
    private PayProperties payProperties;

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            WxpayUnifiedOrderReqBO wxReqBO = (WxpayUnifiedOrderReqBO)reqBO;
            WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request();
            request.setNotifyUrl(String.format(payProperties.getNotifyUrl(), PayConstant.IF_CODE.WXPAY.toLowerCase() + "V3"));
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
            amount.setTotal(reqBO.getPayMoney());
            request.setAmount(amount);
            WxPayUnifiedOrderV3Result.JsapiResult result = this.wxPayService.createOrderV3(TradeTypeEnum.APP, request);
            WxV3PayUnifiedOrderRespBO respBO = new WxV3PayUnifiedOrderRespBO();
            BeanUtils.copyProperties(result, respBO);
            return respBO;
        } catch (WxPayException e) {
            log.info("【微信支付-统一下单】- 下单失败", e);
            throw new BizException(e.getMessage());
        }
    }

    @Override
    public Pair<String, ChannelRetMsg> parseNoticeParamsAndCheck(HttpServletRequest request) throws Exception {

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

        ChannelRetMsg channelResult = new ChannelRetMsg();
        channelResult.setChannelState(ChannelRetMsg.ChannelState.WAITING); // 默认支付中

        String channelState = result.getTradeState();
        if ("SUCCESS".equals(channelState)) {
            channelResult.setChannelState(ChannelRetMsg.ChannelState.CONFIRM_SUCCESS);
        }else if("CLOSED".equals(channelState)
                || "REVOKED".equals(channelState)
                || "PAYERROR".equals(channelState)){  //CLOSED—已关闭， REVOKED—已撤销, PAYERROR--支付失败
            channelResult.setChannelState(ChannelRetMsg.ChannelState.CONFIRM_FAIL); //支付失败
        }
        channelResult.setChannelOrderId(result.getTransactionId()); //渠道订单号
        WxPayOrderNotifyV3Result.Payer payer = result.getPayer();
        if (payer != null) {
            channelResult.setChannelUserId(payer.getOpenid()); //支付用户ID
        }
        JSONObject resJSON = new JSONObject();
        resJSON.put("code", "SUCCESS");
        resJSON.put("message", "成功");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        channelResult.setResponseEntity(new ResponseEntity<>(resJSON, httpHeaders, HttpStatus.OK)); //响应数据

        return Pair.of(result.getOutTradeNo(), channelResult);
    }

}
