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
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayContext;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayKit;
import vip.yeee.memo.integrate.thirdsdk.pay.properties.PayProperties;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/22 18:04
 */
@Slf4j
@Component
public class WxNativeV3PayKit extends WxV3PayKit {

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.WX_NATIVE;
    }

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            PayContext payContext = PayContext.getContext();
            WxPayUnifiedOrderV3Request request = super.buildUnifiedOrderRequest(reqBO);
            request.setNotifyUrl(getPayNotifyUrl());
            WxPayUnifiedOrderV3Result response = payContext.getWxPayService().createOrderV3(TradeTypeEnum.NATIVE, request);
            String codeUrl = response.getCodeUrl();
            CommonUnifiedOrderRespBO respBO = new CommonUnifiedOrderRespBO();
            ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
            respBO.setChannelRetMsg(retMsgBO);
            respBO.setMchId(payContext.getWxPayConfig().getSubMchId());
            if (PayConstant.PAY_DATA_TYPE.CODE_IMG_URL.equals(reqBO.getPayDataType())) {
                PayProperties payProperties = PayContext.getContext().getPayProperties();
                respBO.setCodeImgUrl(payProperties.getSiteUrl() + "/general/img/qr/" + PayKit.aes.encryptHex(codeUrl) + ".png");
                retMsgBO.setChannelAttach(respBO.getCodeImgUrl());
            } else {
                respBO.setCodeUrl(codeUrl);
                retMsgBO.setChannelAttach(respBO.getCodeUrl());
            }
            // 支付中
            retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.WAITING);
            retMsgBO.setChannelOrderId(response.getPrepayId());
            return respBO;
        } catch (Exception e) {
            log.info("【统一下单-微信NATIVE支付V3】- 下单失败", e);
            throw new BizException(e.getMessage());
        }
    }
}
