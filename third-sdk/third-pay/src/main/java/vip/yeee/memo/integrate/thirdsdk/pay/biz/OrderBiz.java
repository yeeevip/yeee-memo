package vip.yeee.memo.integrate.thirdsdk.pay.biz;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import vip.yeee.memo.integrate.base.model.exception.BizException;
import vip.yeee.memo.integrate.base.util.JacksonUtils;
import vip.yeee.memo.integrate.base.web.utils.HttpRequestUtils;
import vip.yeee.memo.integrate.base.web.utils.SpringContextUtils;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.OrderEnum;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.domain.mysql.entity.Order;
import vip.yeee.memo.integrate.thirdsdk.pay.domain.mysql.mapper.OrderMapper;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bo.*;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.req.CheckPayStateReqVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.req.SubmitOrderReqVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.req.UnifiedOrderReqVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.resp.CheckPayStateResVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.resp.SubmitOrderRespVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.resp.UnifiedOrderRespVO;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayKit;
import vip.yeee.memo.integrate.thirdsdk.pay.properties.PayProperties;
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
    private PayProperties payProperties;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public SubmitOrderRespVO submitOrder(SubmitOrderReqVO reqVO) {
        log.info("【提交订单】- 请求参数：{}", reqVO);
        Order newOrder = new Order();
        newOrder.setCode(SeqUtil.genOrderCode());
        newOrder.setState(OrderEnum.State.STATE_INIT.getCode());
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
        log.info("【统一下单】- 请求参数：{}", reqVO);
        PayKit payKit = this.getPayChannelKit(reqVO.getPayWay());
        if (payKit == null) {
            throw new BizException("不支持的支付方式");
        }
        LambdaQueryWrapper<Order> wrapper = Wrappers.<Order>lambdaQuery()
                .eq(Order::getCode, reqVO.getOrderCode());
        Order order = orderMapper.selectOne(wrapper);
        if (order == null) {
            throw new BizException("订单不存在");
        }
        if (OrderEnum.State.checkEndState(order.getState())) {
            throw new BizException(StrUtil.format("订单状态非法，[{}]", OrderEnum.State.getDescByCode(order.getState())));
        }
        UnifiedOrderReqBO reqBO = this.buildUnifiedOrderReqBO(reqVO);
        reqBO.setExpireTime(order.getExpiredTime());
        reqBO.setOrderSubject(order.getSubjectId().toString());
        UnifiedOrderRespBO respBO = payKit.unifiedOrder(reqBO);
        log.info("【统一下单】- 下单成功：{}", respBO);
        // 更新订单状态
        Order upd = new Order();
        upd.setId(order.getId());
        upd.setState(OrderEnum.State.STATE_ING.getCode());
        upd.setChannel(reqVO.getPayWay().split("_")[0]);
        upd.setPayway(reqVO.getPayWay());
        orderMapper.updateById(upd);
        return respBO;
    }

    public ResponseEntity<Object> handlePayNotify(HttpServletRequest request, String ifCode, String orderId) throws Exception {
        try {
            // 获取对应处理接口
            PayKit payKit = this.getPayChannelKit(ifCode);
            // 解析请求参数并验签
            Pair<String, ChannelRetMsgBO> keyValue = payKit.checkAndParseNoticeParams(request);
            String orderCode = keyValue.getKey();
            ChannelRetMsgBO retMsg = keyValue.getValue();
            if (StrUtil.isBlank(orderCode)) {
                throw new BizException("订单号为空");
            }
            // 数据库中查询订单
            LambdaQueryWrapper<Order> wrapper = Wrappers.<Order>lambdaQuery()
                    .select(Order::getId, Order::getState)
                    .eq(Order::getCode, orderCode);
            Order order = orderMapper.selectOne(wrapper);
            if (order == null) {
                throw new BizException("订单不存在");
            }
            // 验证订单状态
            if (OrderEnum.State.STATE_SUCCESS.getCode().equals(order.getState())) {
                throw new BizException("订单已支付");
            }
            LambdaUpdateWrapper<Order> updateWrapper = Wrappers.<Order>lambdaUpdate()
                    .eq(Order::getId, order.getId())
                    .eq(Order::getState, order.getState());
            //明确成功
            if (ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS.equals(retMsg.getChannelState())) {
                updateWrapper.set(Order::getState, OrderEnum.State.STATE_SUCCESS.getCode());
                orderMapper.update(null, updateWrapper);
            } else if (ChannelRetMsgBO.ChannelState.CONFIRM_FAIL.equals(retMsg.getChannelState())) { //明确失败
                updateWrapper.set(Order::getState, OrderEnum.State.STATE_FAIL.getCode());
                orderMapper.update(null, updateWrapper);
            }
            return retMsg.getResponseEntity();
        } catch (BizException e) {
            log.warn("【支付回调通知】- 业务异常", e);
            return this.getDefaultSuccessResp(ifCode);
        }
    }

    private PayKit getPayChannelKit(String payChannel) {
        String beanName;
        if (payChannel.equals(PayConstant.IF_CODE.WXPAY)
                && PayConstant.PAY_IF_VERSION.WX_V3.equals(payProperties.getWx().getApiVersion())) {
            beanName = StrUtil.toCamelCase(payChannel) + "V3PayKit";
        } else {
            beanName = StrUtil.toCamelCase(payChannel) + "PayKit";
        }
        try {
            return (PayKit)SpringContextUtils.getBean(beanName);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
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

    private ResponseEntity<Object> getDefaultSuccessResp(String ifCode) {
        if (PayConstant.IF_CODE.WXPAY.equalsIgnoreCase(ifCode)) {
            return PayKit.getWxV2SuccessResp();
        } else if ((PayConstant.IF_CODE.ALIPAY + PayConstant.PAY_IF_VERSION.WX_V3).equalsIgnoreCase(ifCode)) {
            return PayKit.getWxV3SuccessResp();
        } else if (PayConstant.IF_CODE.ALIPAY.equalsIgnoreCase(ifCode)) {
            return PayKit.getAliSuccessResp();
        }
        return null;
    }

    public CheckPayStateResVO checkOrderPayState(CheckPayStateReqVO request) {
        Order selected = new Order();
        // 获取对应处理接口
        PayKit payKit = this.getPayChannelKit(selected.getPayway());
        QueryOrderReqBO query = new QueryOrderReqBO();
        query.setOrderCode(selected.getCode());
        ChannelRetMsgBO retMsgBO = payKit.queryOrder(query);
        CheckPayStateResVO resVO = new CheckPayStateResVO();
        if (ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS.equals(retMsgBO.getChannelState())) {
            resVO.setPayState(1);
        } else {
            resVO.setPayState(0);
        }
        return resVO;
    }

    public ResponseEntity<Object> handleRefundNotify(HttpServletRequest request, String ifCode) throws Exception {
        try {
            // 获取对应处理接口
            PayKit payKit = this.getPayChannelKit(ifCode);
            // 解析请求参数并验签
            Pair<String, ChannelRetMsgBO> keyValue = payKit.checkAndParseNoticeParams(request);
            String orderCode = keyValue.getKey();
            ChannelRetMsgBO retMsg = keyValue.getValue();
            if (StrUtil.isBlank(orderCode)) {
                throw new BizException("订单号为空");
            }
            // 数据库中查询订单
            LambdaQueryWrapper<Order> wrapper = Wrappers.<Order>lambdaQuery()
                    .select(Order::getId, Order::getState)
                    .eq(Order::getCode, orderCode);
            Order order = orderMapper.selectOne(wrapper);
            if (order == null) {
                throw new BizException("订单不存在");
            }
            // 验证订单状态
            if (OrderEnum.State.STATE_REFUND.getCode().equals(order.getState())) {
                throw new BizException("订单已退款");
            }
            LambdaUpdateWrapper<Order> updateWrapper = Wrappers.<Order>lambdaUpdate()
                    .eq(Order::getId, order.getId())
                    .eq(Order::getState, order.getState());
            //明确成功
            if (ChannelRetMsgBO.ChannelState.CONFIRM_SUCCESS.equals(retMsg.getChannelState())) {
                updateWrapper.set(Order::getRefundState, OrderEnum.State.STATE_REFUND.getCode());
                orderMapper.update(null, updateWrapper);
            } else if (ChannelRetMsgBO.ChannelState.CONFIRM_FAIL.equals(retMsg.getChannelState())) { //明确失败
                updateWrapper.set(Order::getRefundState, OrderEnum.State.STATE_REFUND.getCode());
                orderMapper.update(null, updateWrapper);
            }
            return retMsg.getResponseEntity();
        } catch (BizException e) {
            log.warn("【退款回调通知】- 业务异常", e);
            return this.getDefaultSuccessResp(ifCode);
        }
    }
}
