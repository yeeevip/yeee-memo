package vip.yeee.memo.demo.thirdsdk.pay.paykit.wxpay.v2;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.base.model.exception.BizException;
import vip.yeee.memo.demo.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.ChannelRetMsgBO;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.CommonUnifiedOrderRespBO;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.UnifiedOrderReqBO;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.UnifiedOrderRespBO;
import vip.yeee.memo.demo.thirdsdk.pay.paykit.PayContext;
import vip.yeee.memo.demo.thirdsdk.pay.paykit.PayKit;
import vip.yeee.memo.demo.thirdsdk.pay.properties.PayProperties;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/12/22 17:45
 */
@Slf4j
@Component
public class WxNativePayKit extends BaseWxPayKit {

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.WX_NATIVE;
    }

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            WxPayUnifiedOrderRequest request = super.buildUnifiedOrderRequest(reqBO);
            request.setTradeType(WxPayConstants.TradeType.NATIVE);
            request.setNotifyUrl(getPayNotifyUrl());
            WxPayUnifiedOrderResult response = PayContext.getContext().getWxPayService().unifiedOrder(request);
            String codeUrl = response.getCodeURL();
            CommonUnifiedOrderRespBO respBO = new CommonUnifiedOrderRespBO();
            ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
            respBO.setChannelRetMsg(retMsgBO);
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
            log.info("【统一下单-微信NATIVE支付】- 下单失败 reqBO = {}", reqBO, e);
            throw new BizException(e.getMessage());
        }
    }
}
