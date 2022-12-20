package vip.yeee.memo.integrate.thirdsdk.pay.paykit.alipay;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.ChannelRetMsgBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.CommonUnifiedOrderRespBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.UnifiedOrderReqBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.UnifiedOrderRespBO;
import vip.yeee.memo.integrate.thirdsdk.pay.utils.AmountUtil;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/22 11:31
 */
@Slf4j
@Component
public class AliBarPayKit extends AbstractAliPayKit {

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.ALI_BAR;
    }

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            AlipayTradePayRequest req = new AlipayTradePayRequest();
            AlipayTradePayModel model = new AlipayTradePayModel();
            model.setOutTradeNo(reqBO.getOrderCode());
            model.setScene("bar_code"); //条码支付 bar_code ; 声波支付 wave_code
            model.setAuthCode(reqBO.getAuthCode()); //支付授权码
            model.setSubject(reqBO.getOrderSubject()); //订单标题
            model.setBody(reqBO.getOrderDesc());
            model.setTotalAmount(AmountUtil.convertCent2Dollar(reqBO.getPayMoney().toString()));
            model.setTimeExpire(LocalDateTimeUtil.format(reqBO.getExpireTime(), DatePattern.NORM_DATETIME_PATTERN));
//            req.setNotifyUrl(String.format(payProperties.getNotifyUrl(), PayConstant.PAY_WAY_CODE.ALI_BAR.toLowerCase()));
            req.setBizModel(model);

            CommonUnifiedOrderRespBO respBO = new CommonUnifiedOrderRespBO();
            ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
            respBO.setChannelRetMsg(retMsgBO);

            AlipayTradePayResponse alipayResp = alipayClient.execute(req);
            retMsgBO.setChannelAttach(alipayResp.getBody());
            retMsgBO.setChannelOrderId(alipayResp.getTradeNo());
            retMsgBO.setChannelUserId(alipayResp.getBuyerUserId());
            // 当条码重复发起时，支付宝返回的code = 10003, subCode = null [等待用户支付], 此时需要特殊判断 = = 。
            if ("10000".equals(alipayResp.getCode()) && alipayResp.isSuccess()) {
                retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS);
            } else if ("10003".equals(alipayResp.getCode())) { //10003 表示为 处理中, 例如等待用户输入密码
                retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.WAITING);
            } else { // 其他状态, 表示下单失败
                retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.CONFIRM_FAIL);
                retMsgBO.setChannelErrCode(StrUtil.emptyToDefault(alipayResp.getCode(), alipayResp.getSubCode()));
                retMsgBO.setChannelErrMsg(alipayResp.getMsg() + "##" + alipayResp.getSubMsg());
            }
            return respBO;
        } catch (Exception e) {
            log.info("【统一下单-支付宝BAR支付】- 下单失败 reqBO = {}", reqBO, e);
            throw new BizException(e.getMessage());
        }
    }

}
