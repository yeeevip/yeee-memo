package vip.yeee.memo.integrate.thirdsdk.pay.paykit.alipay;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.ChannelRetMsgBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.CommonUnifiedOrderRespBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.UnifiedOrderReqBO;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayKit;
import vip.yeee.memo.integrate.thirdsdk.pay.utils.AmountUtil;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/22 10:18
 */
@Slf4j
@Component
public class AliWapPayKit extends AbstractAliPayKit {

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.ALI_WAP;
    }

    @Override
    public CommonUnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            AlipayTradeWapPayRequest req = new AlipayTradeWapPayRequest();
            AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
            model.setOutTradeNo(reqBO.getOrderCode());
            model.setSubject(reqBO.getOrderCode()); //订单标题
            model.setBody(reqBO.getOrderDesc()); //订单描述信息
            model.setTotalAmount(AmountUtil.convertCent2Dollar(reqBO.getPayMoney().toString()));  //支付金额
            model.setTimeExpire(LocalDateTimeUtil.format(reqBO.getExpireTime(), DatePattern.NORM_DATETIME_PATTERN));  // 订单超时时间
            model.setProductCode("QUICK_WAP_PAY");
            req.setNotifyUrl(getPayNotifyUrl(reqBO.getOrderCode())); // 设置异步通知地址
            req.setReturnUrl(String.format(payProperties.getReturnUrl(), PayConstant.PAY_WAY_CODE.ALI_WAP.toLowerCase())); // 同步跳转地址
            req.setBizModel(model);

            CommonUnifiedOrderRespBO respBO = new CommonUnifiedOrderRespBO();
            ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
            respBO.setChannelRetMsg(retMsgBO);
            if (PayConstant.PAY_DATA_TYPE.FORM.equals(reqBO.getPayDataType())) {
                respBO.setFormContent(alipayClient.pageExecute(req).getBody());
            } else if (PayConstant.PAY_DATA_TYPE.CODE_IMG_URL.equals(reqBO.getPayDataType())) {
                String payUrl = alipayClient.pageExecute(req, "GET").getBody();
                respBO.setCodeImgUrl(payProperties.getSiteUrl() + "/general/img/qr/" + PayKit.aes.encryptHex(payUrl) + ".png");
            } else {
                respBO.setPayUrl(alipayClient.pageExecute(req, "GET").getBody());
            }
            retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.WAITING);
            return respBO;
        } catch (AlipayApiException e) {
            log.info("【统一下单-支付宝WAP支付】- 下单失败 reqBO = {}", reqBO, e);
            throw new BizException(e.getMessage());
        }
    }
}
