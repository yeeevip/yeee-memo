package vip.yeee.memo.integrate.thirdsdk.pay.paykit.wxpay.v2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.*;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/22 17:26
 */
@Slf4j
@Component
public class WxBarPayKit extends AbstractWxPayKit {

    @Resource
    private WxBarPayKit wxBarPayKit;

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.WX_BAR;
    }

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            return wxBarPayKit.unifiedOrder(reqBO);
        } catch (Exception e) {
            log.info("【统一下单-微信BAR支付】- 下单失败", e);
            throw new BizException(e.getMessage());
        }
    }

}
