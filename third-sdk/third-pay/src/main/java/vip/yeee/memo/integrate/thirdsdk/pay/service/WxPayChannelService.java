package vip.yeee.memo.integrate.thirdsdk.pay.service;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.common.model.exception.BizException;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.UnifiedOrderReqBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.UnifiedOrderRespBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.WxpayUnifiedOrderReqBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.WxpayUnifiedOrderRespBO;
import vip.yeee.memo.integrate.thirdsdk.pay.properties.WxpayProperties;

import javax.annotation.Resource;

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
    private WxpayProperties wxpayProperties;

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            WxpayUnifiedOrderReqBO wxReqBO = (WxpayUnifiedOrderReqBO)reqBO;
            WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
            request.setSpbillCreateIp(wxReqBO.getClientIp());
            request.setTradeType(wxReqBO.getTradeType());
            request.setNotifyUrl(wxpayProperties.getNotifyUrl());
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

}
