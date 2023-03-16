package vip.yeee.memo.integrate.thirdsdk.pay.paykit;

import cn.hutool.core.lang.Pair;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.google.common.collect.ImmutableMap;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.*;
import vip.yeee.memo.integrate.thirdsdk.pay.properties.WxPayConfig;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 16:42
 */
public interface PayKit {

    String getPayway();

    UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO);

    ChannelRetMsgBO queryOrder(QueryOrderReqBO reqBO);

    ChannelRetMsgBO closeOrder(CloseOrderReqBO reqBO);

    ChannelRetMsgBO refundOrder(RefundOrderReqBO reqBO);

    ChannelRetMsgBO transfer(TransferReqBO reqBO);

    Pair<String, ChannelRetMsgBO> checkAndParsePayNoticeParams(HttpServletRequest request) throws Exception;

    Pair<String, ChannelRetMsgBO> checkAndParseRefundNoticeParams(HttpServletRequest request) throws Exception;

    AES aes = SecureUtil.aes("4ChT08phkz501wwD795X7w==".getBytes());

    static ResponseEntity<Object> getWxV2SuccessResp(String text) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<>(WxPayNotifyResponse.successResp(text), httpHeaders, HttpStatus.OK);
    }

    static ResponseEntity<Object> getWxV3SuccessResp(String text) {
        Map<String, String> map = ImmutableMap.of("code", text, "message", "成功");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(map, httpHeaders, HttpStatus.OK);
    }

    static ResponseEntity<Object> getAliSuccessResp(String text) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<>(text, httpHeaders, HttpStatus.OK);
    }

    static ResponseEntity<Object> getDefaultSuccessResp(String ifCode) {
        if (ifCode.startsWith(PayConstant.IF_CODE.WXPAY.toLowerCase())) {
            WxPayConfig wxPayConfig = PayContext.getContext().getWxPayConfig();
            if (PayConstant.PAY_IF_VERSION.WX_V3.equals(wxPayConfig.getApiVersion())) {
                return PayKit.getWxV3SuccessResp("ERROR");
            }
            return PayKit.getWxV2SuccessResp("OK");
        } else if (ifCode.startsWith(PayConstant.IF_CODE.ALIPAY.toLowerCase())) {
            return PayKit.getAliSuccessResp("SUCCESS");
        }
        return null;
    }

    static ResponseEntity<Object> getDefaultErrorResp(String ifCode) {
        if (ifCode.startsWith(PayConstant.IF_CODE.WXPAY.toLowerCase())) {
            WxPayConfig wxPayConfig = PayContext.getContext().getWxPayConfig();
            if (PayConstant.PAY_IF_VERSION.WX_V3.equals(wxPayConfig.getApiVersion())) {
                return PayKit.getWxV3SuccessResp("ERROR");
            }
            return PayKit.getWxV2SuccessResp("ERROR");
        } else if (ifCode.startsWith(PayConstant.IF_CODE.ALIPAY.toLowerCase())) {
            return PayKit.getAliSuccessResp("ERROR");
        }
        return null;
    }

}
