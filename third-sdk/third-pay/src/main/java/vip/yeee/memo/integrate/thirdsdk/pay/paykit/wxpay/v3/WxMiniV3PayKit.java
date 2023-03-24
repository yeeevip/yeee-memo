package vip.yeee.memo.integrate.thirdsdk.pay.paykit.wxpay.v3;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.UnifiedOrderReqBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.UnifiedOrderRespBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.WxPayConfigBO;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayContext;
import vip.yeee.memo.integrate.thirdsdk.pay.properties.WxPayConfig;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/22 18:04
 */
@Slf4j
@Component
public class WxMiniV3PayKit extends WxV3PayKit {

    @Resource
    private WxWpV3PayKit wxWpV3PayKit;

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.WX_MINI;
    }

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        return wxWpV3PayKit.unifiedOrder(reqBO);
    }

    @Override
    protected WxPayUnifiedOrderV3Request buildUnifiedOrderRequest(UnifiedOrderReqBO reqBO) {
        WxPayUnifiedOrderV3Request request = super.buildUnifiedOrderRequest(reqBO);
        WxPayConfigBO wxPayConfig = PayContext.getContext().getWxPayConfig();
        request.setAppid(wxPayConfig.getAppId());
        return request;
    }
}
