package vip.yeee.memo.integrate.thirdsdk.pay.paykit.wxpay.v3;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.ChannelRetMsgBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.CommonUnifiedOrderRespBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.UnifiedOrderReqBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.UnifiedOrderRespBO;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayKit;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/22 18:04
 */
@Slf4j
@Component
public class WxH5V3PayKiy extends AbstractWxV3PayKit {

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.WX_H5;
    }

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            WxPayUnifiedOrderV3Request request = super.buildUnifiedOrderRequest(reqBO);
            request.setNotifyUrl(getPayNotifyUrl(reqBO.getOrderCode()));
            WxPayUnifiedOrderV3Result response = this.wxPayService.createOrderV3(TradeTypeEnum.H5, request);
            String payUrl = response.getH5Url();

            CommonUnifiedOrderRespBO respBO = new CommonUnifiedOrderRespBO();
            ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
            respBO.setChannelRetMsg(retMsgBO);
            if (PayConstant.PAY_DATA_TYPE.CODE_IMG_URL.equals(reqBO.getPayDataType())) {
                respBO.setCodeImgUrl(payProperties.getSiteUrl() + "/general/img/qr/" + PayKit.aes.encryptHex(payUrl) + ".png");
            } else {
                respBO.setPayUrl(payUrl);
            }
            // 支付中
            retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.WAITING);
            return respBO;
        } catch (Exception e) {
            log.info("【统一下单-微信H5支付V3】- 下单失败", e);
            throw new BizException(e.getMessage());
        }
    }
}
