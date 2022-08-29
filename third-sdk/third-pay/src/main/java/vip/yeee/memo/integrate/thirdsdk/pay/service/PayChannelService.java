package vip.yeee.memo.integrate.thirdsdk.pay.service;

import cn.hutool.core.lang.Pair;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.google.common.collect.ImmutableMap;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import vip.yeee.memo.integrate.thirdsdk.pay.model.notice.ChannelRetMsg;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.UnifiedOrderReqBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.UnifiedOrderRespBO;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 16:42
 */
public interface PayChannelService {

    UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO);

    Pair<String, ChannelRetMsg> parseNoticeParamsAndCheck(HttpServletRequest request) throws Exception;

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
