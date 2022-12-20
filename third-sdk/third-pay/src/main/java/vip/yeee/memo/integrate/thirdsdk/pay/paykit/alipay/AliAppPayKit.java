package vip.yeee.memo.integrate.thirdsdk.pay.paykit.alipay;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.AliAppUnifiedOrderRespBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.ChannelRetMsgBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.UnifiedOrderReqBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.UnifiedOrderRespBO;
import vip.yeee.memo.integrate.thirdsdk.pay.utils.AmountUtil;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/20 17:23
 */
@Slf4j
@Component
public class AliAppPayKit extends AbstractAliPayKit {

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.ALI_APP;
    }

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            AlipayTradeAppPayRequest req = new AlipayTradeAppPayRequest();
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setOutTradeNo(reqBO.getOrderCode());
            model.setSubject(reqBO.getOrderCode()); //订单标题
            model.setBody(reqBO.getOrderDesc()); //订单描述信息
            model.setTotalAmount(AmountUtil.convertCent2Dollar(reqBO.getPayMoney().toString()));  //支付金额
            model.setTimeExpire(LocalDateTimeUtil.format(reqBO.getExpireTime(), DatePattern.NORM_DATETIME_PATTERN));  // 订单超时时间
            req.setNotifyUrl(getPayNotifyUrl(reqBO.getOrderCode())); // 设置异步通知地址
            req.setBizModel(model);

            AliAppUnifiedOrderRespBO respBO = new AliAppUnifiedOrderRespBO();
            ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
            respBO.setChannelRetMsg(retMsgBO);
            // 调用sdkExecute生成orderStr（未真正请求支付宝服务端）
            // 用于商户客户端将orderStr传给支付宝APP（SDK）调用支付宝服务端进行支付预下单唤起支付宝收银台
            respBO.setPayData(alipayClient.sdkExecute(req).getBody());
            retMsgBO.setChannelAttach(respBO.getPayData());
            retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.WAITING);
            return respBO;
        } catch (Exception e) {
            log.info("【统一下单-支付宝APP支付】- 下单失败 reqBO = {}", reqBO,  e);
            // sdk方式需自行拦截接口异常信息
            throw new BizException(e.getMessage());
        }
    }
}
