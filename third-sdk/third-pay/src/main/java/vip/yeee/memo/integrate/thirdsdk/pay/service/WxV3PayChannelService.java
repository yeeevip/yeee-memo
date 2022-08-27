package vip.yeee.memo.integrate.thirdsdk.pay.service;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.common.model.exception.BizException;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.*;
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
public class WxV3PayChannelService implements PayChannelService {

    @Resource
    private WxPayService wxPayService;
    @Resource
    private WxpayProperties wxpayProperties;

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            WxpayUnifiedOrderReqBO wxReqBO = (WxpayUnifiedOrderReqBO)reqBO;
            WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request();
            request.setNotifyUrl(wxpayProperties.getNotifyUrl());
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

}
