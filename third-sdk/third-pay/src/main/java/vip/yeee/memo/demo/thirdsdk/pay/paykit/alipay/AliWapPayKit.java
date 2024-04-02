package vip.yeee.memo.demo.thirdsdk.pay.paykit.alipay;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.base.model.exception.BizException;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.AliPayConfigBO;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.ChannelRetMsgBO;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.CommonUnifiedOrderRespBO;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.UnifiedOrderReqBO;
import vip.yeee.memo.demo.thirdsdk.pay.paykit.PayContext;
import vip.yeee.memo.demo.thirdsdk.pay.paykit.PayKit;
import vip.yeee.memo.demo.thirdsdk.pay.properties.PayProperties;
import vip.yeee.memo.demo.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.demo.thirdsdk.pay.utils.AmountUtil;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/12/22 10:18
 */
@Slf4j
@Component
public class AliWapPayKit extends BaseAliPayKit {

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.ALI_WAP;
    }

    @Override
    public CommonUnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            PayContext payContext = PayContext.getContext();
            PayProperties payProperties = PayContext.getContext().getPayProperties();
            AliPayConfigBO aliPayConfig = payContext.getAliPayConfig();
            AlipayTradeWapPayRequest req = new AlipayTradeWapPayRequest();
            req.setReturnUrl(String.format(payProperties.getReturnUrl(), PayConstant.PAY_WAY_CODE.ALI_WAP.toLowerCase())); // 同步跳转地址
            if (StrUtil.isNotBlank(aliPayConfig.getAuthToken())) {
                req.putOtherTextParam("app_auth_token", aliPayConfig.getAuthToken());
            }
            AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
            model.setOutTradeNo(reqBO.getOrderCode());
            model.setSubject(reqBO.getOrderCode()); //订单标题
            model.setBody(reqBO.getOrderDesc()); //订单描述信息
            model.setTotalAmount(AmountUtil.convertCent2Dollar(reqBO.getPayMoney().toString()));  //支付金额
            model.setTimeExpire(LocalDateTimeUtil.format(reqBO.getExpireTime(), DatePattern.NORM_DATETIME_PATTERN));  // 订单超时时间
            model.setProductCode("QUICK_WAP_PAY");
            model.setQuitUrl(req.getReturnUrl());
            req.setNotifyUrl(getPayNotifyUrl()); // 设置异步通知地址
            req.setBizModel(model);

            CommonUnifiedOrderRespBO respBO = new CommonUnifiedOrderRespBO();
            ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
            respBO.setChannelRetMsg(retMsgBO);
            respBO.setMchId(payContext.getAliPayConfig().getSubAppId());
            AlipayClient alipayClient = payContext.getAlipayClient();
            if (PayConstant.PAY_DATA_TYPE.FORM.equals(reqBO.getPayDataType())) {
                AlipayTradeWapPayResponse response = alipayClient.pageExecute(req);
                respBO.setFormContent(response.getBody());
                retMsgBO.setChannelAttach(respBO.getFormContent());
                retMsgBO.setChannelOrderId(response.getTradeNo());
            } else if (PayConstant.PAY_DATA_TYPE.CODE_IMG_URL.equals(reqBO.getPayDataType())) {
                AlipayTradeWapPayResponse response = alipayClient.pageExecute(req, "GET");
                respBO.setCodeImgUrl(payProperties.getSiteUrl() + "/general/img/qr/" + PayKit.aes.encryptHex(response.getBody()) + ".png");
                retMsgBO.setChannelAttach(respBO.getCodeImgUrl());
                retMsgBO.setChannelOrderId(response.getTradeNo());
            } else {
                AlipayTradeWapPayResponse response = alipayClient.pageExecute(req, "GET");
                respBO.setPayUrl(response.getBody());
                retMsgBO.setChannelAttach(respBO.getPayUrl());
                retMsgBO.setChannelOrderId(response.getTradeNo());
            }
            retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.WAITING);
            return respBO;
        } catch (AlipayApiException e) {
            log.info("【统一下单-支付宝WAP支付】- 下单失败 reqBO = {}", reqBO, e);
            throw new BizException(e.getMessage());
        }
    }
}
