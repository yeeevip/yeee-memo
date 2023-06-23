package vip.yeee.memo.demo.thirdsdk.pay.service;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vip.yeee.memo.base.model.exception.BizException;
import vip.yeee.memo.base.web.utils.SpringContextUtils;
import vip.yeee.memo.common.web.utils.HttpRequestUtils;
import vip.yeee.memo.demo.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.demo.thirdsdk.pay.model.bo.*;
import vip.yeee.memo.demo.thirdsdk.pay.paykit.PayContext;
import vip.yeee.memo.demo.thirdsdk.pay.paykit.PayKit;
import vip.yeee.memo.demo.thirdsdk.pay.model.vo.req.UnifiedOrderQueryReqVO;
import vip.yeee.memo.demo.thirdsdk.pay.model.vo.req.UnifiedOrderRefundReqVO;
import vip.yeee.memo.demo.thirdsdk.pay.model.vo.req.UnifiedOrderReqVO;

import java.util.function.Function;

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
        try {
            PayContext.setContext(reqVO.getLesseeId());
            log.info("【统一下单】- 请求参数：{}", reqVO);
            PayKit payKit = this.getPayChannelKit(reqVO.getPayway());
            UnifiedOrderReqBO reqBO = this.buildUnifiedOrderReqBO(reqVO);
            UnifiedOrderRespBO respBO = payKit.unifiedOrder(reqBO);
            log.info("【统一下单】- 下单成功：{}", respBO);
            return respBO;
        } finally {
            PayContext.clearContext();
        }
    }

    public ChannelRetMsgBO unifiedOrderRefund(UnifiedOrderRefundReqVO reqVO) {
        try {
            PayContext.setContext(reqVO.getLesseeId());
            log.info("【统一退款】- 请求参数：{}", reqVO);
            PayKit payKit = this.getPayChannelKit(reqVO.getPayway());
            ChannelRetMsgBO retMsgBO = payKit.refundOrder(this.buildUnifiedOrderRefundReqBO(reqVO));
            log.info("【统一退款】- 退款成功：{}", retMsgBO);
            return retMsgBO;
        } finally {
            PayContext.clearContext();
        }
    }

    public ChannelRetMsgBO queryOrder(UnifiedOrderQueryReqVO request) {
        try {
            PayContext.setContext(request.getLesseeId());
            // 获取对应处理接口
            PayKit payKit = this.getPayChannelKit(request.getPayway());
            QueryOrderReqBO query = new QueryOrderReqBO();
            query.setOrderCode(request.getOrderCode());
            ChannelRetMsgBO retMsgBO = payKit.queryOrder(query);
            return retMsgBO;
        } finally {
            PayContext.clearContext();
        }
    }

    public ResponseEntity<Object> payNoticeHandle(String ifCode, String lesseeId, Function<Pair<String, ChannelRetMsgBO>, ResponseEntity<Object>> handler) {
        try {
            PayContext.setContext(lesseeId);
            // 获取对应处理接口
            PayKit payKit = this.getPayChannelKit(ifCode.toUpperCase());
            // 解析请求参数并验签
            Pair<String, ChannelRetMsgBO> keyValue = payKit.checkAndParsePayNoticeParams();
            return handler.apply(keyValue);
        } catch (Exception e) {
            if (!(e instanceof BizException)) {
                log.error("【支付回调通知】- 业务异常", e);
                return PayKit.getDefaultErrorResp(ifCode);
            }
            log.warn("【支付回调通知】- 系统异常 - " + e.getMessage());
            return PayKit.getDefaultSuccessResp(ifCode);
        } finally {
            PayContext.clearContext();
        }
    }

    public ResponseEntity<Object> refundNoticeHandle(String ifCode, String lesseeId, Function<Pair<String, ChannelRetMsgBO>, ResponseEntity<Object>> handler) {
        try {
            PayContext.setContext(lesseeId);
            // 获取对应处理接口
            PayKit payKit = this.getPayChannelKit(ifCode.toUpperCase());
            // 解析请求参数并验签
            Pair<String, ChannelRetMsgBO> keyValue = payKit.checkAndParseRefundNoticeParams();
            return handler.apply(keyValue);
        } catch (Exception e) {
            if (!(e instanceof BizException)) {
                log.error("【退款回调通知】- 业务异常", e);
                return PayKit.getDefaultErrorResp(ifCode);
            }
            log.warn("【退款回调通知】- 系统异常 - " + e.getMessage());
            return PayKit.getDefaultSuccessResp(ifCode);
        } finally {
            PayContext.clearContext();
        }
    }

    private PayKit getPayChannelKit(String payWay) {
        return PayContext.getPayChannelKit(payWay);
    }

    private UnifiedOrderReqBO buildUnifiedOrderReqBO(UnifiedOrderReqVO reqVO) {
        String payWay = reqVO.getPayway();
        if (PayConstant.PAY_WAY_CODE.ALI_BAR.equals(reqVO.getPayway())
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
