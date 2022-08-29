package vip.yeee.memo.integrate.thirdsdk.pay.service;

import cn.hutool.core.lang.Pair;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.common.model.exception.BizException;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.model.notice.ChannelRetMsg;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.UnifiedOrderReqBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.UnifiedOrderRespBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.WxpayUnifiedOrderReqBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.WxpayUnifiedOrderRespBO;
import vip.yeee.memo.integrate.thirdsdk.pay.properties.PayProperties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 10:52
 */
@Slf4j
@Component
public class WxPayChannelService implements PayChannelService {

    @Resource
    private WxPayService wxPayService;
    @Resource
    private PayProperties payProperties;

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            WxpayUnifiedOrderReqBO wxReqBO = (WxpayUnifiedOrderReqBO)reqBO;
            WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
            request.setSpbillCreateIp(wxReqBO.getClientIp());
            request.setTradeType(wxReqBO.getTradeType());
            request.setNotifyUrl(String.format(payProperties.getNotifyUrl(), PayConstant.IF_CODE.WXPAY.toLowerCase()));
            request.setOutTradeNo(wxReqBO.getOrderCode());
            // 商品描述
            request.setBody(wxReqBO.getOrderDesc());
            request.setTotalFee(wxReqBO.getPayMoney());
            request.setAttach(wxReqBO.getExtraInfo());
            WxPayUnifiedOrderResult result = wxPayService.unifiedOrder(request);
            WxpayUnifiedOrderRespBO respBO = new WxpayUnifiedOrderRespBO();
            respBO.setTradeType(result.getTradeType());
            respBO.setPrepayId(result.getPrepayId());
            return respBO;
        } catch (WxPayException e) {
            log.info("【微信支付-统一下单】- 下单失败", e);
            throw new BizException(e.getMessage());
        }
    }

    @Override
    public Pair<String, ChannelRetMsg> parseNoticeParamsAndCheck(HttpServletRequest request) throws Exception {
        String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        WxPayOrderNotifyResult result = WxPayOrderNotifyResult.fromXML(xmlResult);
        String orderCode = result.getOutTradeNo();
        log.info("解析数据为：orderCode = {}, params = {}", orderCode, result);
        // 验签
        result.checkResult(wxPayService, WxPayConstants.SignType.MD5, true);
        ChannelRetMsg channelResult = new ChannelRetMsg();
        channelResult.setChannelState(ChannelRetMsg.ChannelState.WAITING); // 默认支付中
        channelResult.setChannelOrderId(result.getTransactionId()); //渠道订单号
        channelResult.setChannelUserId(result.getOpenid()); //支付用户ID
        channelResult.setChannelState(ChannelRetMsg.ChannelState.CONFIRM_SUCCESS);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_HTML);
        channelResult.setResponseEntity(new ResponseEntity<>(WxPayNotifyResponse.successResp("OK"), httpHeaders, HttpStatus.OK));
        return Pair.of(orderCode, channelResult);
    }

}
