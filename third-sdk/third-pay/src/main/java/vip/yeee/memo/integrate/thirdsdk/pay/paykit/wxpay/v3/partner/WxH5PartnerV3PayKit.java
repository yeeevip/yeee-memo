package vip.yeee.memo.integrate.thirdsdk.pay.paykit.wxpay.v3.partner;

import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.*;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayContext;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayKit;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.wxpay.v3.partner.request.WxPayUnifiedOrderV3PartnerRequest;
import vip.yeee.memo.integrate.thirdsdk.pay.properties.PayProperties;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/22 18:04
 */
@Slf4j
@Component
public class WxH5PartnerV3PayKit extends WxPartnerV3PayKit {

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.WX_H5;
    }

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            PayContext payContext = PayContext.getContext();
            WxPayService wxPayService = payContext.getWxPayService();
            PayProperties payProperties = payContext.getPayProperties();
            WxPayUnifiedOrderV3PartnerRequest request = super.buildPartnerUnifiedOrderRequest(reqBO);
            WxPayUnifiedOrderV3PartnerRequest.SceneInfo sceneInfo = new WxPayUnifiedOrderV3PartnerRequest.SceneInfo();
            sceneInfo.setPayerClientIp(((WxpayUnifiedOrderReqBO)reqBO).getClientIp());
            WxPayUnifiedOrderV3PartnerRequest.H5Info h5Info = new WxPayUnifiedOrderV3PartnerRequest.H5Info();
            h5Info.setType("Wap");
            sceneInfo.setH5Info(h5Info);
            request.setSceneInfo(sceneInfo);
            String url = wxPayService.getPayBaseUrl() + "/v3/pay/partner/transactions/h5";
            String responseStr = wxPayService.postV3(url, GSON.toJson(request));
            WxPayUnifiedOrderV3Result response = GSON.fromJson(responseStr, WxPayUnifiedOrderV3Result.class);
            String payUrl = response.getH5Url();

            CommonUnifiedOrderRespBO respBO = new CommonUnifiedOrderRespBO();
            ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
            respBO.setChannelRetMsg(retMsgBO);
            respBO.setMchId(payContext.getWxPayConfig().getSubMchId());
            if (PayConstant.PAY_DATA_TYPE.CODE_IMG_URL.equals(reqBO.getPayDataType())) {
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
            log.info("【统一下单-微信H5支付V3】- 下单失败", e);
            throw new BizException(e.getMessage());
        }
    }
}
