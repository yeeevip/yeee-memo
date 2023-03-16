package vip.yeee.memo.integrate.thirdsdk.pay.service;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.base.web.utils.HttpRequestUtils;
import vip.yeee.memo.integrate.base.web.utils.SpringContextUtils;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.*;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.req.OrderQueryReqVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.req.UnifiedOrderRefundReqVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.req.UnifiedOrderReqVO;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayContext;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayKit;

import javax.servlet.http.HttpServletRequest;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/3/17 9:49
 */
@Slf4j
@Service
public class UnifiedPayOrderService {

    public UnifiedOrderRespBO unifiedOrder(UnifiedOrderReqVO reqVO) throws Exception {
        PayContext.setContext("1111");
        log.info("【统一下单】- 请求参数：{}", reqVO);
        PayKit payKit = this.getPayChannelKit(reqVO.getPayWay());
        UnifiedOrderReqBO reqBO = this.buildUnifiedOrderReqBO(reqVO);
        UnifiedOrderRespBO respBO = payKit.unifiedOrder(reqBO);
        log.info("【统一下单】- 下单成功：{}", respBO);
        PayContext.clearContext();
        return respBO;
    }

    public ChannelRetMsgBO unifiedOrderRefund(UnifiedOrderRefundReqVO reqVO) {
        PayContext.setContext("1111");
        log.info("【统一退款】- 请求参数：{}", reqVO);
        PayKit payKit = this.getPayChannelKit(reqVO.getPayway());
        ChannelRetMsgBO retMsgBO = payKit.refundOrder(this.buildUnifiedOrderRefundReqBO(reqVO));
        log.info("【统一退款】- 退款成功：{}", retMsgBO);
        PayContext.clearContext();
        return retMsgBO;
    }

    public ChannelRetMsgBO queryOrder(OrderQueryReqVO request) {
        PayContext.setContext("1111");
        // 获取对应处理接口
        PayKit payKit = this.getPayChannelKit(request.getPayway());
        QueryOrderReqBO query = new QueryOrderReqBO();
        query.setOrderCode(request.getOrderCode());
        ChannelRetMsgBO retMsgBO = payKit.queryOrder(query);
        PayContext.clearContext();
        return retMsgBO;
    }

    public Pair<String, ChannelRetMsgBO> parsePayNoticeParams(HttpServletRequest request, String ifCode, String lesseeId) throws Exception {
        PayContext.setContext("1111");
        // 获取对应处理接口
        PayKit payKit = this.getPayChannelKit(ifCode.toUpperCase());
        // 解析请求参数并验签
        Pair<String, ChannelRetMsgBO> keyValue = payKit.checkAndParsePayNoticeParams(request);
        return keyValue;
    }

    public Pair<String, ChannelRetMsgBO> parseRefundNoticeParams(HttpServletRequest request, String ifCode, String lesseeId) throws Exception {
        PayContext.setContext("1111");
        // 获取对应处理接口
        PayKit payKit = this.getPayChannelKit(ifCode.toUpperCase());
        // 解析请求参数并验签
        Pair<String, ChannelRetMsgBO> keyValue = payKit.checkAndParseRefundNoticeParams(request);
        return keyValue;
    }

    private PayKit getPayChannelKit(String payWay) {
        return PayContext.getPayChannelKit(payWay);
    }

    private UnifiedOrderReqBO buildUnifiedOrderReqBO(UnifiedOrderReqVO reqVO) {
        String payWay = reqVO.getPayWay();
        if (PayConstant.PAY_WAY_CODE.ALI_BAR.equals(reqVO.getPayWay())
                && StrUtil.isBlank(reqVO.getAuthCode())) {
            throw new BizException("用户支付条码不能为空");
        }
        if (payWay.startsWith(PayConstant.IF_CODE.WXPAY)) {
            WxpayUnifiedOrderReqBO reqBO = new WxpayUnifiedOrderReqBO();
            BeanUtils.copyProperties(reqVO, reqBO);
            reqBO.setPayMoney(reqVO.getAmount().intValue());
            String clientIp = HttpRequestUtils.getIpAddr(SpringContextUtils.getHttpServletRequest());
            reqBO.setClientIp(clientIp);
            return reqBO;
        } else if (payWay.startsWith(PayConstant.IF_CODE.ALIPAY)) {
            AlipayUnifiedOrderReqBO reqBO = new AlipayUnifiedOrderReqBO();
            BeanUtils.copyProperties(reqVO, reqBO);
            reqBO.setPayMoney(reqVO.getAmount().intValue());
            return reqBO;
        } else {
            UnifiedOrderReqBO reqBO = new UnifiedOrderReqBO();
            BeanUtils.copyProperties(reqVO, reqBO);
            return reqBO;
        }
    }

    private RefundOrderReqBO buildUnifiedOrderRefundReqBO(UnifiedOrderRefundReqVO reqVO) {
        RefundOrderReqBO reqBO = new RefundOrderReqBO();
        BeanUtils.copyProperties(reqVO, reqBO);
        return reqBO;
    }
}
