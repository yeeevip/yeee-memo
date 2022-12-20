package vip.yeee.memo.integrate.thirdsdk.pay.paykit.wxpay.v2;

import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.*;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayKit;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/22 17:45
 */
@Slf4j
@Component
public class WxNativePayKit extends AbstractWxPayKit {

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.WX_NATIVE;
    }

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            WxPayUnifiedOrderRequest request = super.buildUnifiedOrderRequest(reqBO);
            request.setTradeType(WxPayConstants.TradeType.NATIVE);
            request.setNotifyUrl(getPayNotifyUrl(reqBO.getOrderCode()));
            WxPayNativeOrderResult response =  wxPayService.createOrder(request);
            String codeUrl = response.getCodeUrl();
            CommonUnifiedOrderRespBO respBO = new CommonUnifiedOrderRespBO();
            ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
            respBO.setChannelRetMsg(retMsgBO);
            if (PayConstant.PAY_DATA_TYPE.CODE_IMG_URL.equals(reqBO.getPayDataType())) {
                respBO.setCodeImgUrl(payProperties.getSiteUrl() + "/general/img/qr/" + PayKit.aes.encryptHex(codeUrl) + ".png");
            } else {
                respBO.setCodeUrl(codeUrl);
            }
            // 支付中
            retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.WAITING);
            return respBO;
        } catch (Exception e) {
            log.info("【统一下单-微信NATIVE支付】- 下单失败 reqBO = {}", reqBO, e);
            throw new BizException(e.getMessage());
        }
    }
}
