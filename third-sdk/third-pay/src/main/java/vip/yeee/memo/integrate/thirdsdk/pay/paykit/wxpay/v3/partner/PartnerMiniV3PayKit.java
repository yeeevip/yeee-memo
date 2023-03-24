package vip.yeee.memo.integrate.thirdsdk.pay.paykit.wxpay.v3.partner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.UnifiedOrderReqBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.UnifiedOrderRespBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.WxPayConfigBO;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayContext;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.wxpay.v3.partner.request.WxPayUnifiedOrderV3PartnerRequest;
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
public class PartnerMiniV3PayKit extends PartnerWxV3PayKit {

    @Resource
    private PartnerWxWpV3PayKit partnerWxWpV3PayKit;

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.WX_MINI;
    }

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        return partnerWxWpV3PayKit.unifiedOrder(reqBO);
    }

    @Override
    protected WxPayUnifiedOrderV3PartnerRequest buildPartnerUnifiedOrderRequest(UnifiedOrderReqBO reqBO) {
        WxPayUnifiedOrderV3PartnerRequest request = super.buildPartnerUnifiedOrderRequest(reqBO);
        WxPayConfigBO wxPayConfig = PayContext.getContext().getWxPayConfig();
        request.setSpAppid(wxPayConfig.getMiniAppId());
        request.setSubAppId(wxPayConfig.getSubMiniAppId());
        return request;
    }
}
