package vip.yeee.memo.demo.thirdsdk.pay.paykit.alipay;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.base.model.exception.BizException;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.*;
import vip.yeee.memo.demo.thirdsdk.pay.paykit.PayContext;
import vip.yeee.memo.demo.thirdsdk.pay.paykit.PayKit;
import vip.yeee.memo.demo.thirdsdk.pay.properties.PayProperties;
import vip.yeee.memo.demo.thirdsdk.pay.constant.OrderEnum;
import vip.yeee.memo.demo.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.demo.thirdsdk.pay.utils.AmountUtil;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/12/22 15:25
 */
@Slf4j
@Component
public class AliQrPayKit extends BaseAliPayKit {

    @Override
    public String getPayway() {
        return PayConstant.PAY_WAY_CODE.ALI_QR;
    }

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        try {
            PayContext payContext = PayContext.getContext();
            PayProperties payProperties = payContext.getPayProperties();
            AliPayConfigBO aliPayConfig = payContext.getAliPayConfig();
            AlipayTradePrecreateRequest req = new AlipayTradePrecreateRequest();
            if (StrUtil.isNotBlank(aliPayConfig.getAuthToken())) {
                req.putOtherTextParam("app_auth_token", aliPayConfig.getAuthToken());
            }
            AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
            model.setOutTradeNo(reqBO.getOrderCode());
            model.setSubject(reqBO.getOrderSubject());
            model.setBody(reqBO.getOrderDesc());
            model.setTotalAmount(AmountUtil.convertCent2Dollar(reqBO.getPayMoney().toString()));
            model.setTimeExpire(LocalDateTimeUtil.format(reqBO.getExpireTime(), DatePattern.NORM_DATETIME_PATTERN));
            req.setNotifyUrl(getPayNotifyUrl());
            req.setBizModel(model);

            CommonUnifiedOrderRespBO respBO = new CommonUnifiedOrderRespBO();
            ChannelRetMsgBO retMsgBO = new ChannelRetMsgBO();
            respBO.setChannelRetMsg(retMsgBO);
            respBO.setMchId(payContext.getAliPayConfig().getSubAppId());

            AlipayTradePrecreateResponse alipayResp = payContext.getAlipayClient().execute(req);
            if (alipayResp.isSuccess()) {
                if(PayConstant.PAY_DATA_TYPE.CODE_IMG_URL.equals(reqBO.getPayDataType())) {
                    respBO.setCodeImgUrl(payProperties.getSiteUrl() + "/general/img/qr/" + PayKit.aes.encryptHex(alipayResp.getQrCode()) + ".png");
                    retMsgBO.setChannelAttach(respBO.getCodeImgUrl());
                } else {
                    respBO.setCodeUrl(alipayResp.getQrCode());
                    retMsgBO.setChannelAttach(respBO.getCodeUrl());
                }
                retMsgBO.setChannelState(ChannelRetMsgBO.ChannelState.WAITING);
            } else {
                respBO.setOrderState(OrderEnum.PayState.FAIL.getCode());
                retMsgBO.setChannelErrCode(StrUtil.emptyToDefault(alipayResp.getCode(), alipayResp.getSubCode()));
                retMsgBO.setChannelErrMsg(alipayResp.getMsg() + "##" + alipayResp.getSubMsg());
            }
//            retMsgBO.setChannelOrderId(alipayResp.get());
            return respBO;
        } catch (Exception e) {
            log.info("【统一下单-支付宝QR支付】- 下单失败 reqBO = {}", reqBO, e);
            throw new BizException(e.getMessage());
        }
    }
}
