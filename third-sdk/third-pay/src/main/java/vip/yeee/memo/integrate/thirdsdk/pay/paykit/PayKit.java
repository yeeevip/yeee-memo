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
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.*;

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

    Pair<String, ChannelRetMsgBO> checkAndParseNoticeParams(HttpServletRequest request) throws Exception;

    AES aes = SecureUtil.aes("4ChT08phkz501wwD795X7w==".getBytes());

    static ResponseEntity<Object> getWxV2SuccessResp() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<>(WxPayNotifyResponse.successResp("OK"), httpHeaders, HttpStatus.OK);
    }

    static ResponseEntity<Object> getWxV3SuccessResp() {
        Map<String, String> map = ImmutableMap.of("code", "SUCCESS", "message", "成功");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(map, httpHeaders, HttpStatus.OK);
    }

    static ResponseEntity<Object> getAliSuccessResp() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<>("SUCCESS", httpHeaders, HttpStatus.OK);
    }

}
