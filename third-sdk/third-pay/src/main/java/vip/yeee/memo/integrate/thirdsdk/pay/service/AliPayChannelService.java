package vip.yeee.memo.integrate.thirdsdk.pay.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.common.model.exception.BizException;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.AlipayUnifiedOrderRespBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.UnifiedOrderReqBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.UnifiedOrderRespBO;
import vip.yeee.memo.integrate.thirdsdk.pay.properties.AlipayProperties;
import vip.yeee.memo.integrate.thirdsdk.pay.utils.AmountUtil;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 10:53
 */
@Component
public class AliPayChannelService implements PayChannelService {

    @Resource
    private AlipayProperties alipayProperties;
    @Resource
    private AlipayClient alipayClient;

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        AlipayTradeAppPayRequest req = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setOutTradeNo(reqBO.getOrderCode());
        model.setSubject(reqBO.getOrderCode()); //订单标题
        model.setBody(reqBO.getOrderDesc()); //订单描述信息
        model.setTotalAmount(AmountUtil.convertCent2Dollar(reqBO.getPayMoney().toString()));  //支付金额
        model.setTimeExpire(LocalDateTimeUtil.format(reqBO.getExpireTime(), DatePattern.NORM_DATETIME_PATTERN));  // 订单超时时间
        req.setNotifyUrl(alipayProperties.getNotifyUrl()); // 设置异步通知地址
        req.setBizModel(model);

        String payData = null;

        // sdk方式需自行拦截接口异常信息
        try {
            payData = alipayClient.sdkExecute(req).getBody();
        } catch (AlipayApiException e) {
            throw new BizException(e.getMessage());
        }
        AlipayUnifiedOrderRespBO respBO = new AlipayUnifiedOrderRespBO();
        respBO.setPayData(payData);
        return respBO;
    }

}
