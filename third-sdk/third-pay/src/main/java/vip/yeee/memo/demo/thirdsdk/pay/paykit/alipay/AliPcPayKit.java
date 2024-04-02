package vip.yeee.memo.demo.thirdsdk.pay.paykit.alipay;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.base.model.exception.BizException;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.AliPayConfigBO;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.ChannelRetMsgBO;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.CommonUnifiedOrderRespBO;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.UnifiedOrderReqBO;
import vip.yeee.memo.demo.thirdsdk.pay.paykit.PayContext;
import vip.yeee.memo.demo.thirdsdk.pay.properties.PayProperties;
import vip.yeee.memo.demo.thirdsdk.pay.utils.AmountUtil;
import vip.yeee.memo.demo.thirdsdk.pay.constant.PayConstant;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/12/21 11:30
 */
@Slf4j
@Component
public class AliPcPayKit extends BaseAliPayKit {

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.ALI_PC;
    }

    @Override
    public CommonUnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            PayContext payContext = PayContext.getContext();
            PayProperties payProperties = payContext.getPayProperties();
            AliPayConfigBO aliPayConfig = payContext.getAliPayConfig();
            AlipayTradePagePayRequest req = new AlipayTradePagePayRequest();
            if (StrUtil.isNotBlank(aliPayConfig.getAuthToken())) {
                req.putOtherTextParam("app_auth_token", aliPayConfig.getAuthToken());
            }
            AlipayTradePagePayModel model = new AlipayTradePagePayModel();
            model.setOutTradeNo(reqBO.getOrderCode());
            model.setSubject(reqBO.getOrderCode()); //订单标题
            model.setBody(reqBO.getOrderDesc()); //订单描述信息
            model.setTotalAmount(AmountUtil.convertCent2Dollar(reqBO.getPayMoney().toString()));  //支付金额
            model.setTimeExpire(LocalDateTimeUtil.format(reqBO.getExpireTime(), DatePattern.NORM_DATETIME_PATTERN));  // 订单超时时间
            model.setProductCode("FAST_INSTANT_TRADE_PAY");
            model.setQrPayMode("2"); //订单码-跳转模式
            req.setNotifyUrl(getPayNotifyUrl()); // 设置异步通知地址
            req.setReturnUrl(String.format(payProperties.getReturnUrl(), PayConstant.PAY_WAY_CODE.ALI_PC.toLowerCase())); // 同步跳转地址
            req.setBizModel(model);

            CommonUnifiedOrderRespBO respBO = new CommonUnifiedOrderRespBO();
            ChannelRetMsgBO channelRetMsg = new ChannelRetMsgBO();
            respBO.setChannelRetMsg(channelRetMsg);
            respBO.setMchId(payContext.getAliPayConfig().getSubAppId());
            if(PayConstant.PAY_DATA_TYPE.FORM.equals(reqBO.getPayDataType())) {
                AlipayTradePagePayResponse response = payContext.getAlipayClient().pageExecute(req);
                respBO.setFormContent(response.getBody());
                channelRetMsg.setChannelAttach(respBO.getFormContent());
                channelRetMsg.setChannelOrderId(response.getTradeNo());
            } else {
                AlipayTradePagePayResponse response = payContext.getAlipayClient().pageExecute(req, "GET");
                respBO.setPayUrl(response.getBody());
                channelRetMsg.setChannelAttach(respBO.getPayUrl());
                channelRetMsg.setChannelOrderId(response.getTradeNo());
            }
            channelRetMsg.setChannelState(ChannelRetMsgBO.ChannelState.WAITING);
            return respBO;
        } catch (Exception e) {
            log.info("【统一下单-支付宝PC支付】- 下单失败 reqBO = {}", reqBO, e);
            throw new BizException(e.getMessage());
        }
    }
}
