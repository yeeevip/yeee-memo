package vip.yeee.memo.demo.thirdsdk.pay.paykit.wxpay.v3;

import com.github.binarywang.wxpay.bean.request.WxPayMicropayRequest;
import com.github.binarywang.wxpay.bean.result.WxPayMicropayResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.base.model.exception.BizException;
import vip.yeee.memo.demo.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.*;
import vip.yeee.memo.demo.thirdsdk.pay.paykit.PayContext;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/22 18:03
 */
@Slf4j
@Component
public class WxBarV3PayKit extends WxV3PayKit {

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.WX_BAR;
    }

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            WxpayUnifiedOrderReqBO wxReqBO = (WxpayUnifiedOrderReqBO)reqBO;
            WxPayMicropayRequest request = new WxPayMicropayRequest();
            request.setOutTradeNo(wxReqBO.getOrderCode());
            request.setBody(wxReqBO.getOrderSubject());
            request.setDetail(wxReqBO.getOrderDesc());
            request.setFeeType("CNY");
            request.setTotalFee(wxReqBO.getPayMoney());
            request.setSpbillCreateIp(wxReqBO.getClientIp());
            request.setAuthCode(wxReqBO.getAuthCode().trim());

            CommonUnifiedOrderRespBO respBO = new CommonUnifiedOrderRespBO();
            ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
            respBO.setChannelRetMsg(retMsgBO);
            try {
                WxPayMicropayResult response = PayContext.getContext().getWxPayService().micropay(request);
                retMsgBO.setChannelOrderId(response.getTransactionId());
                retMsgBO.setChannelUserId(response.getOpenid());
                retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS);
            } catch (WxPayException e) {
                //微信返回支付状态为【支付结果未知】, 需进行查单操作
                if ("SYSTEMERROR".equals(e.getErrCode())
                        || "USERPAYING".equals(e.getErrCode())
                        ||  "BANKERROR".equals(e.getErrCode())) {
                    //轮询查询订单
                    retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.WAITING);
                    retMsgBO.setNeedQuery(true);
                } else {
                    retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_FAIL);
                    super.commonSetErrInfo(retMsgBO, e);
                }
            }
            return respBO;
        } catch (Exception e) {
            log.info("【统一下单-微信BAR支付V3】- 下单失败 reqBO = {}", reqBO, e);
            throw new BizException(e.getMessage());
        }
    }
}
