package vip.yeee.memo.integrate.thirdsdk.pay.paykit.wxpay.v2;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.*;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayContext;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayKit;
import vip.yeee.memo.integrate.thirdsdk.pay.properties.PayProperties;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/22 17:13
 */
@Slf4j
@Component
public class WxH5PayKit extends BaseWxPayKit {

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.WX_H5;
    }

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            PayContext payContext = PayContext.getContext();
            WxPayUnifiedOrderRequest request = super.buildUnifiedOrderRequest(reqBO);
            request.setTradeType(WxPayConstants.TradeType.MWEB);
            request.setNotifyUrl(getPayNotifyUrl());
            WxPayUnifiedOrderResult response = payContext.getWxPayService().unifiedOrder(request);
            String payUrl = response.getMwebUrl();

            CommonUnifiedOrderRespBO respBO = new CommonUnifiedOrderRespBO();
            ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
            respBO.setChannelRetMsg(retMsgBO);
            respBO.setMchId(payContext.getWxPayConfig().getSubMchId());
            if (PayConstant.PAY_DATA_TYPE.CODE_IMG_URL.equals(reqBO.getPayDataType())) {
                PayProperties payProperties = PayContext.getContext().getPayProperties();
                respBO.setCodeImgUrl(payProperties.getSiteUrl() + "/general/img/qr/" + PayKit.aes.encryptHex(payUrl) + ".png");
                retMsgBO.setChannelAttach(respBO.getCodeImgUrl());
            } else {
                respBO.setPayUrl(payUrl);
                retMsgBO.setChannelAttach(respBO.getPayUrl());
            }
            // 支付中
            retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.WAITING);
            retMsgBO.setChannelOrderId(response.getPrepayId());
            return respBO;
        } catch (Exception e) {
            log.info("【统一下单-微信H5支付】- 下单失败 reqBO = {}", reqBO, e);
            throw new BizException(e.getMessage());
        }
    }

}
