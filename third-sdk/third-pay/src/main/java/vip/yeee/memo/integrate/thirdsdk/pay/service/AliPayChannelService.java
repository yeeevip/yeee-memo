package vip.yeee.memo.integrate.thirdsdk.pay.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.extra.servlet.ServletUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.common.base.utils.JacksonUtils;
import vip.yeee.memo.integrate.common.model.exception.BizException;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.model.notice.ChannelRetMsg;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.AlipayUnifiedOrderRespBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.UnifiedOrderReqBO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.UnifiedOrderRespBO;
import vip.yeee.memo.integrate.thirdsdk.pay.properties.PayProperties;
import vip.yeee.memo.integrate.thirdsdk.pay.utils.AmountUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 10:53
 */
@Component
public class AliPayChannelService implements PayChannelService {

    @Resource
    private PayProperties payProperties;
    @Resource
    private AlipayClient alipayClient;

    @Override
    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqBO reqBO) {
        AlipayTradeAppPayRequest req = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setOutTradeNo(reqBO.getOrderCode());
        model.setSubject(reqBO.getOrderCode()); //订单标题
        model.setBody(reqBO.getOrderDesc()); //订单描述信息
        model.setTotalAmount(AmountUtil.convertCent2Dollar(reqBO.getPayMoney().toString()));  //支付金额
        model.setTimeExpire(LocalDateTimeUtil.format(reqBO.getExpireTime(), DatePattern.NORM_DATETIME_PATTERN));  // 订单超时时间
        req.setNotifyUrl(String.format(payProperties.getNotifyUrl(), PayConstant.IF_CODE.ALIPAY.toLowerCase())); // 设置异步通知地址
        req.setBizModel(model);

        String payData = null;

        // sdk方式需自行拦截接口异常信息
        try {
            payData = alipayClient.sdkExecute(req).getBody();
        } catch (AlipayApiException e) {
            throw new BizException(e.getMessage());
        }
        AlipayUnifiedOrderRespBO respBO = new AlipayUnifiedOrderRespBO();
        respBO.setPayData(payData);
        return respBO;
    }

    @Override
    public Pair<String, ChannelRetMsg> parseNoticeParamsAndCheck(HttpServletRequest request) throws Exception {
        Map<String, String> map = JacksonUtils.toJavaBean(ServletUtil.getBody(request), new TypeReference<Map<String, String>>() {});
        PayProperties.AlipayProperties ali = payProperties.getAli();
        boolean verifyResult;
        if (ali.getUseCert()) {  //证书方式
            verifyResult = AlipaySignature.rsaCertCheckV1(map, ali.getAlipayPublicCert(), "utf-8", ali.getSignType());
        } else {
            verifyResult = AlipaySignature.rsaCheckV1(map, ali.getAlipayPublicCert(), "utf-8", ali.getSignType());
        }
        if (!verifyResult) {
            throw new Exception("验签失败");
        }
        //验签成功后判断上游订单状态
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.TEXT_HTML);
        ResponseEntity<Object> okResponse = new ResponseEntity<>("SUCCESS", httpHeaders, HttpStatus.OK);

        ChannelRetMsg result = new ChannelRetMsg();
        result.setChannelOrderId(map.get("trade_no")); //渠道订单号
        result.setChannelUserId(map.get("buyer_id")); //支付用户ID
        result.setResponseEntity(okResponse); //响应数据

        result.setChannelState(ChannelRetMsg.ChannelState.WAITING); // 默认支付中

        if("TRADE_SUCCESS".equals(map.get("trade_status"))){
            result.setChannelState(ChannelRetMsg.ChannelState.CONFIRM_SUCCESS);

        }else if("TRADE_CLOSED".equals(map.get("trade_status"))){
            result.setChannelState(ChannelRetMsg.ChannelState.CONFIRM_FAIL);

        }
        return Pair.of(map.get("out_trade_no"), result);
    }

}
