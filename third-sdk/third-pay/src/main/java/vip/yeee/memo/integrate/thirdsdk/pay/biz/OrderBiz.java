package vip.yeee.memo.integrate.thirdsdk.pay.biz;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.OrderEnum;
import vip.yeee.memo.integrate.thirdsdk.pay.domain.mysql.entity.Order;
import vip.yeee.memo.integrate.thirdsdk.pay.domain.mysql.mapper.OrderMapper;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.*;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.req.OrderQueryReqVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.req.SubmitOrderReqVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.req.UnifiedOrderRefundReqVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.req.UnifiedOrderReqVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.resp.CheckPayStateResVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.resp.SubmitOrderRespVO;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayContext;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayKit;
import vip.yeee.memo.integrate.thirdsdk.pay.service.UnifiedPayOrderService;
import vip.yeee.memo.integrate.thirdsdk.pay.utils.SeqUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    private OrderMapper orderMapper;
    @Resource
    private UnifiedPayOrderService unifiedPayOrderService;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public SubmitOrderRespVO submitOrder(SubmitOrderReqVO reqVO) {
        log.info("【提交订单】- 请求参数：{}", reqVO);
        Order newOrder = new Order();
        newOrder.setCode(SeqUtil.genOrderCode());
        newOrder.setState(OrderEnum.PayState.INIT.getCode());
        newOrder.setSubjectId(reqVO.getSubjectId());
        newOrder.setUserId(10000010L);
        newOrder.setAmount(reqVO.getAmount());
        newOrder.setCurrency("cny");
        newOrder.setExpiredTime(LocalDateTimeUtil.offset(LocalDateTime.now(), 15, ChronoUnit.MINUTES));
        orderMapper.insert(newOrder);
        SubmitOrderRespVO orderRespVO = new SubmitOrderRespVO(newOrder.getCode(), newOrder.getAmount().toString(), newOrder.getExpiredTime());
        log.info("【提交订单】- 下单成功：{}", orderRespVO);
        return orderRespVO;
    }

    public Object unifiedOrder(UnifiedOrderReqVO reqVO) throws Exception {
        LambdaQueryWrapper<Order> wrapper = Wrappers.<Order>lambdaQuery()
                .eq(Order::getCode, reqVO.getOrderCode());
        Order order = orderMapper.selectOne(wrapper);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        if (OrderEnum.PayState.checkEndState(order.getState())) {
            throw new BizException(StrUtil.format("订单状态非法，[{}]"
                    , OrderEnum.PayState.getDescByCode(order.getState())));
        }
        UnifiedOrderRespBO respBO = unifiedPayOrderService.unifiedOrder(reqVO);
        // 更新订单状态
        Order upd = new Order();
        upd.setId(order.getId());
        upd.setState(OrderEnum.PayState.ING.getCode());
        upd.setChannel(reqVO.getPayWay().split("_")[0]);
        upd.setPayway(reqVO.getPayWay());
        orderMapper.updateById(upd);
        PayContext.clearContext();
        return respBO;
    }

    public Void unifiedOrderRefund(UnifiedOrderRefundReqVO reqVO) {
        LambdaQueryWrapper<Order> wrapper = Wrappers.<Order>lambdaQuery()
                .eq(Order::getCode, reqVO.getPayOrderCode());
        Order order = orderMapper.selectOne(wrapper);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        ChannelRetMsgBO retMsgBO = unifiedPayOrderService.unifiedOrderRefund(reqVO);
        log.info("【统一退款】- 退款：{}", retMsgBO);
        if (ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS.equals(retMsgBO.getChannelState())) {
            log.info("【统一退款】- 退款成功");
        } else if (ChannelRetMsgBO.ChannelState.WAITING.equals(retMsgBO.getChannelState())) {
            log.info("【统一退款】- 退款处理中");
        } else {
            log.info("【统一退款】- 退款失败");
        }
        return null;
    }

    public CheckPayStateResVO checkOrderQuery(OrderQueryReqVO request) {
        Order selected = new Order();
        ChannelRetMsgBO retMsgBO = unifiedPayOrderService.queryOrder(request);
        CheckPayStateResVO resVO = new CheckPayStateResVO();
        if (ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS.equals(retMsgBO.getChannelState())) {
            resVO.setPayState(1);
        } else {
            resVO.setPayState(0);
        }
        return resVO;
    }

    public ResponseEntity<Object> handlePayNotify(HttpServletRequest request, String ifCode, String lesseeId) throws Exception {
        try {
            Pair<String, ChannelRetMsgBO> keyValue = unifiedPayOrderService.parsePayNoticeParams(request, ifCode, lesseeId);
            String orderCode = keyValue.getKey();
//            // 微信支付为保证回调通知触达有效性，会有保障策略，在第一次通知如果网络链路返回无法连接或者状态不明，微信支付会换一条链路进行通知。
//            boolean canDo = checkRepeatKit.canRepeatPayNotify(orderCode, 15);
//            if (!canDo) {
//                throw new Exception("重复操作");
//            }
            ChannelRetMsgBO retMsg = keyValue.getValue();
            if (StrUtil.isBlank(orderCode)) {
                throw new BizException("订单号为空");
            }
            LambdaQueryWrapper<Order> wrapper = Wrappers.<Order>lambdaQuery()
                    .select(Order::getId, Order::getState)
                    .eq(Order::getCode, orderCode);
            Order order = orderMapper.selectOne(wrapper);
            if (order == null) {
                throw new BizException("订单不存在");
            }
            // 验证订单状态
            // 支付宝退款后 会回调订单关闭状态通知
            if (!OrderEnum.PayState.ING.getCode().equals(order.getState())) {
                throw new BizException("订单状态有误");
            }
            LambdaUpdateWrapper<Order> updateWrapper = Wrappers.<Order>lambdaUpdate()
                    .eq(Order::getId, order.getId())
                    .eq(Order::getState, order.getState());
            if (StrUtil.isNotBlank(retMsg.getChannelOrderId())) {
                updateWrapper.set(Order::getChannelOrderNo, retMsg.getChannelOrderId());
            }
            //明确成功
            if (ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS.equals(retMsg.getChannelState())) {
                updateWrapper.set(Order::getState, OrderEnum.PayState.SUCCESS.getCode());
                orderMapper.update(null, updateWrapper);
            } else if (ChannelRetMsgBO.ChannelState.CONFIRM_FAIL.equals(retMsg.getChannelState())) { //明确失败
                updateWrapper.set(Order::getState, OrderEnum.PayState.FAIL.getCode());
                orderMapper.update(null, updateWrapper);
            }
            return retMsg.getResponseEntity();
        } catch (Exception e) {
            if (!(e instanceof BizException)) {
                log.error("【支付回调通知】- 业务异常", e);
                return PayKit.getDefaultErrorResp(ifCode);
            }
            log.warn("【支付回调通知】- 业务异常 - " + e.getMessage());
            return PayKit.getDefaultSuccessResp(ifCode);
        } finally {
            PayContext.clearContext();
        }
    }

    public ResponseEntity<Object> handleRefundNotify(HttpServletRequest request, String ifCode, String lesseeId) throws Exception {
        try {
            Pair<String, ChannelRetMsgBO> keyValue = unifiedPayOrderService.parseRefundNoticeParams(request, ifCode, lesseeId);
            String orderCode = keyValue.getKey();
//            // 微信支付为保证回调通知触达有效性，会有保障策略，在第一次通知如果网络链路返回无法连接或者状态不明，微信支付会换一条链路进行通知。
//            boolean canDo = checkRepeatKit.canRepeatPayNotify(orderCode, 15);
//            if (!canDo) {
//                throw new Exception("重复操作");
//            }
            ChannelRetMsgBO retMsg = keyValue.getValue();
            if (StrUtil.isBlank(orderCode)) {
                throw new BizException("订单号为空");
            }
            LambdaQueryWrapper<Order> wrapper = Wrappers.<Order>lambdaQuery()
                    .select(Order::getId, Order::getState)
                    .eq(Order::getCode, orderCode);
            Order order = orderMapper.selectOne(wrapper);
            if (order == null) {
                throw new BizException("订单不存在");
            }
            // 验证订单状态
            if (!OrderEnum.PayState.CANCEL.getCode().equals(order.getState())) {
                throw new BizException("订单状态有误");
            }
            LambdaUpdateWrapper<Order> updateWrapper = Wrappers.<Order>lambdaUpdate()
                    .eq(Order::getId, order.getId())
                    .eq(Order::getState, order.getState());
            //明确成功
            if (ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS.equals(retMsg.getChannelState())) {
                updateWrapper.set(Order::getRefundState, OrderEnum.PayState.REFUND.getCode());
                orderMapper.update(null, updateWrapper);
            } else if (ChannelRetMsgBO.ChannelState.CONFIRM_FAIL.equals(retMsg.getChannelState())) { //明确失败
                updateWrapper.set(Order::getRefundState, OrderEnum.PayState.REFUND_FAIL.getCode());
                orderMapper.update(null, updateWrapper);
            }
            return retMsg.getResponseEntity();
        } catch (Exception e) {
            if (!(e instanceof BizException)) {
                log.error("【退款回调通知】- 业务异常", e);
                return PayKit.getDefaultErrorResp(ifCode);
            }
            log.warn("【退款回调通知】- 业务异常 - " + e.getMessage());
            return PayKit.getDefaultSuccessResp(ifCode);
        } finally {
            PayContext.clearContext();
        }
    }
}
