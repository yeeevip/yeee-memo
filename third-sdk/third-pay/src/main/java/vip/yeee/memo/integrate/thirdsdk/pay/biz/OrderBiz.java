package vip.yeee.memo.integrate.thirdsdk.pay.biz;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.OrderEnum;
import vip.yeee.memo.integrate.thirdsdk.pay.domain.mysql.entity.PayOrder;
import vip.yeee.memo.integrate.thirdsdk.pay.domain.mysql.entity.RefundOrder;
import vip.yeee.memo.integrate.thirdsdk.pay.domain.mysql.mapper.PayOrderMapper;
import vip.yeee.memo.integrate.thirdsdk.pay.domain.mysql.mapper.RefundOrderMapper;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.*;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.req.*;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.resp.CheckPayStateResVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.resp.SubmitOrderRespVO;
import vip.yeee.memo.integrate.thirdsdk.pay.service.UnifiedPayOrderService;
import vip.yeee.memo.integrate.thirdsdk.pay.utils.SeqUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 14:54
 */
@Slf4j
@Component
public class OrderBiz {

    @Resource
    private PayOrderMapper payOrderMapper;
    @Resource
    private RefundOrderMapper refundOrderMapper;
    @Resource
    private UnifiedPayOrderService unifiedPayOrderService;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public SubmitOrderRespVO submitOrder(SubmitOrderReqVO reqVO) {
        log.info("【提交订单】- 请求参数：{}", reqVO);
        PayOrder newOrder = new PayOrder();
        newOrder.setCode(SeqUtil.genOrderCode());
        newOrder.setState(OrderEnum.PayState.INIT.getCode());
        newOrder.setUserId("测试用户ID");
        newOrder.setAmount(reqVO.getAmount());
        newOrder.setCurrency("cny");
        newOrder.setExpiredTime(LocalDateTimeUtil.offset(LocalDateTime.now(), 30, ChronoUnit.MINUTES));
        payOrderMapper.insert(newOrder);
        SubmitOrderRespVO orderRespVO = new SubmitOrderRespVO(newOrder.getCode(), newOrder.getAmount().toString(), newOrder.getExpiredTime());
        log.info("【提交订单】- 下单成功：{}", orderRespVO);
        return orderRespVO;
    }

    public Object orderPay(OrderPayReqVO reqVO) throws Exception {
        LambdaQueryWrapper<PayOrder> wrapper = Wrappers.<PayOrder>lambdaQuery()
                .eq(PayOrder::getCode, reqVO.getOrderCode());
        PayOrder order = payOrderMapper.selectOne(wrapper);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        if (OrderEnum.PayState.checkEndState(order.getState())) {
            throw new BizException(StrUtil.format("订单状态非法，[{}]"
                    , OrderEnum.PayState.getDescByCode(order.getState())));
        }
        if (reqVO.getAmount().compareTo(order.getAmount()) != 0) {
            throw new BizException("金额不正确");
        }
        UnifiedOrderReqVO unifiedOrderReqVO = new UnifiedOrderReqVO();
//        unifiedOrderReqVO.setLesseeId(order.getLesseeId());
//        unifiedOrderReqVO.setOpenId(StrUtil.isNotBlank(reqVO.getOpenId()) ? reqVO.getOpenId() : user.getThirdId());
        unifiedOrderReqVO.setAppId(reqVO.getAppId());
        unifiedOrderReqVO.setPayway(reqVO.getPayway());
        unifiedOrderReqVO.setOrderCode(order.getCode());
        unifiedOrderReqVO.setAmount(order.getAmount());
        unifiedOrderReqVO.setOrderSubject("测试商品下单");
        unifiedOrderReqVO.setOrderDesc("测试商品下单");
        UnifiedOrderRespBO respBO = unifiedPayOrderService.unifiedOrder(unifiedOrderReqVO);
        ChannelRetMsgBO retMsg = respBO.getChannelRetMsg();
        // 更新订单信息
        PayOrder orderUpd = new PayOrder();
        orderUpd.setId(order.getId());
        orderUpd.setPayway(reqVO.getPayway());
        orderUpd.setChannel(StrUtil.subBefore(reqVO.getPayway(), "_", false));
        orderUpd.setOutCode(retMsg.getChannelOrderId());
        orderUpd.setMchId(respBO.getMchId());
        orderUpd.setState(OrderEnum.PayState.ING.getCode());
        payOrderMapper.updateById(orderUpd);
        reqVO.setPayInfo(retMsg.getChannelAttach());
        return reqVO;
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public Void orderRefund(OrderRefundReqVO reqVO) {
        LambdaQueryWrapper<PayOrder> wrapper = Wrappers.<PayOrder>lambdaQuery()
                .eq(PayOrder::getCode, reqVO.getOrderCode());
        PayOrder order = payOrderMapper.selectOne(wrapper);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        RefundOrder saveRefund = new RefundOrder();
        saveRefund.setCode(SeqUtil.genOrderCode());

        UnifiedOrderRefundReqVO orderRefundReqVO = new UnifiedOrderRefundReqVO();
        orderRefundReqVO.setLesseeId(order.getLesseeId());
        orderRefundReqVO.setPayOrderCode(order.getCode());
        orderRefundReqVO.setOutPayOrderId(order.getOutCode());
        orderRefundReqVO.setRefundOrderCode(saveRefund.getCode());
        orderRefundReqVO.setAmount(order.getAmount().multiply(BigDecimal.valueOf(100)).longValue());
        orderRefundReqVO.setRefundAmount(orderRefundReqVO.getAmount());
        orderRefundReqVO.setPayway(order.getPayway());
        ChannelRetMsgBO retMsgBO = unifiedPayOrderService.unifiedOrderRefund(orderRefundReqVO);
        int updPayState;
        if (ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS.equals(retMsgBO.getChannelState())) {
            saveRefund.setState(OrderEnum.RefundState.SUCCESS.getCode());
            updPayState = OrderEnum.PayState.REFUND.getCode();
        } else if (ChannelRetMsgBO.ChannelState.CONFIRM_FAIL.equals(retMsgBO.getChannelState())) {
            saveRefund.setState(OrderEnum.RefundState.FAIL.getCode());
            updPayState = OrderEnum.PayState.REFUND_FAIL.getCode();
        } else {
            saveRefund.setState(OrderEnum.RefundState.ING.getCode());
            updPayState = OrderEnum.PayState.CANCEL.getCode();
        }
        saveRefund.setLesseeId(order.getLesseeId());
        saveRefund.setOutCode(retMsgBO.getChannelOrderId());
        saveRefund.setMchId(order.getMchId());
        saveRefund.setChannel(order.getChannel());
        saveRefund.setOrderCode(order.getCode());
        saveRefund.setAmount(order.getAmount());
        refundOrderMapper.insert(saveRefund);
        int res = payOrderMapper.updateOrderRefundState(order.getId(), order.getState(), updPayState);
        if (res <= 0) {
            throw new BizException("退款时发生异常");
        }
        return null;
    }

    public CheckPayStateResVO orderQuery(OrderQueryReqVO request) {
        LambdaQueryWrapper<PayOrder> wrapper = Wrappers.<PayOrder>lambdaQuery()
                .eq(PayOrder::getCode, request.getOrderCode());
        PayOrder order = payOrderMapper.selectOne(wrapper);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        CheckPayStateResVO resVO = new CheckPayStateResVO();
        resVO.setPayState(order.getState());
        UnifiedOrderQueryReqVO reqVO = new UnifiedOrderQueryReqVO();
        reqVO.setLesseeId(order.getLesseeId());
        reqVO.setOrderCode(order.getCode());
        reqVO.setPayway(order.getPayway());
        ChannelRetMsgBO retMsgBO = unifiedPayOrderService.queryOrder(reqVO);
        PayOrder orderUpd = new PayOrder();
        orderUpd.setId(order.getId());
        if (ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS.equals(retMsgBO.getChannelState())) {
            orderUpd.setState(OrderEnum.PayState.SUCCESS.getCode());
            orderUpd.setSuccessTime(LocalDateTime.now());
            payOrderMapper.updateById(orderUpd);
        } else if (ChannelRetMsgBO.ChannelState.CONFIRM_FAIL.equals(retMsgBO.getChannelState())) {
            orderUpd.setState(OrderEnum.PayState.FAIL.getCode());
            payOrderMapper.updateById(orderUpd);
        } else {
            orderUpd.setState(OrderEnum.PayState.ING.getCode());
        }
        resVO.setPayState(orderUpd.getState());
        return resVO;
    }

    public ResponseEntity<Object> handlePayNotify(String ifCode, String lesseeId) {
        return unifiedPayOrderService.payNoticeHandle(ifCode, lesseeId, p -> {
            String orderCode = p.getKey();
            ChannelRetMsgBO retMsg = p.getValue();
//            // 微信支付为保证回调通知触达有效性，会有保障策略，在第一次通知如果网络链路返回无法连接或者状态不明，微信支付会换一条链路进行通知。
//            boolean canDo = checkRepeatKit.canRepeatPayNotify(orderCode, 15);
//            if (!canDo) {
//                throw new Exception("重复操作");
//            }
            LambdaQueryWrapper<PayOrder> wrapper = Wrappers.<PayOrder>lambdaQuery()
                    .select(PayOrder::getId, PayOrder::getState)
                    .eq(PayOrder::getCode, orderCode);
            PayOrder order = payOrderMapper.selectOne(wrapper);
            if (order == null) {
                throw new BizException("订单不存在");
            }
            // 验证订单状态
            // 支付宝退款后 会回调订单关闭状态通知
            if (!OrderEnum.PayState.ING.getCode().equals(order.getState())) {
                throw new BizException("订单状态有误");
            }
            PayOrder orderUpd = new PayOrder();
            orderUpd.setId(order.getId());
            if (StrUtil.isNotBlank(retMsg.getChannelOrderId())) {
                orderUpd.setOutCode(retMsg.getChannelOrderId());
            }
            //明确成功
            if (ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS.equals(retMsg.getChannelState())) {
                orderUpd.setState(OrderEnum.PayState.SUCCESS.getCode());
                orderUpd.setSuccessTime(LocalDateTime.now());
                payOrderMapper.updateById(orderUpd);
            } else if (ChannelRetMsgBO.ChannelState.CONFIRM_FAIL.equals(retMsg.getChannelState())) { //明确失败
                orderUpd.setState(OrderEnum.PayState.FAIL.getCode());
                payOrderMapper.updateById(orderUpd);
            }
            return retMsg.getResponseEntity();
        });
    }

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public ResponseEntity<Object> handleRefundNotify(String ifCode, String lesseeId) {
        return unifiedPayOrderService.refundNoticeHandle(ifCode, lesseeId, p -> {
            String refundOrderCode = p.getKey();
            ChannelRetMsgBO retMsg = p.getValue();
//            // 微信支付为保证回调通知触达有效性，会有保障策略，在第一次通知如果网络链路返回无法连接或者状态不明，微信支付会换一条链路进行通知。
//            boolean canDo = checkRepeatKit.canRepeatRefundNotify(refundOrderCode, 15);
//            if (!canDo) {
//                throw new Exception("重复操作");
//            }
            LambdaQueryWrapper<RefundOrder> wrapper = Wrappers.<RefundOrder>lambdaQuery()
                    .select(RefundOrder::getId, RefundOrder::getState)
                    .eq(RefundOrder::getCode, refundOrderCode);
            RefundOrder refundOrder = refundOrderMapper.selectOne(wrapper);
            if (refundOrder == null) {
                throw new BizException("退款单不存在");
            }
            LambdaQueryWrapper<PayOrder> payOrderWrapper = Wrappers.<PayOrder>lambdaQuery()
                    .select(PayOrder::getId, PayOrder::getState)
                    .eq(PayOrder::getCode, refundOrder.getOrderCode());
            PayOrder order = payOrderMapper.selectOne(payOrderWrapper);
            // 验证订单状态
            if (!OrderEnum.PayState.CANCEL.getCode().equals(order.getState())) {
                throw new BizException("订单状态有误");
            }
            RefundOrder refundOrderUpd = new RefundOrder();
            refundOrderUpd.setId(refundOrder.getId());
            //明确成功
            if (ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS.equals(retMsg.getChannelState())) {
                refundOrderUpd.setState(OrderEnum.RefundState.SUCCESS.getCode());
                refundOrderUpd.setSuccessTime(LocalDateTime.now());
                this.updateRefundOrder(order, refundOrderUpd);
            } else if (ChannelRetMsgBO.ChannelState.CONFIRM_FAIL.equals(retMsg.getChannelState())) { //明确失败
                refundOrderUpd.setState(OrderEnum.RefundState.FAIL.getCode());
                this.updateRefundOrder(order, refundOrderUpd);
            }
            return retMsg.getResponseEntity();
        });
    }

//    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    private void updateRefundOrder(PayOrder order, RefundOrder refundOrderUpd) {
        refundOrderMapper.updateById(refundOrderUpd);
        if (OrderEnum.RefundState.SUCCESS.getCode().equals(order.getState())) {
            PayOrder payUpd = new PayOrder();
            payUpd.setId(order.getId());
            payUpd.setState(OrderEnum.PayState.REFUND.getCode());
            payOrderMapper.updateById(payUpd);
        } else if (OrderEnum.RefundState.FAIL.getCode().equals(order.getState())) {
            PayOrder payUpd = new PayOrder();
            payUpd.setId(order.getId());
            payUpd.setState(OrderEnum.PayState.REFUND_FAIL.getCode());
            payOrderMapper.updateById(payUpd);
        }
    }
}
