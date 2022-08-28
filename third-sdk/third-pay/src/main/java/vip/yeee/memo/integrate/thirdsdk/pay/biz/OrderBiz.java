package vip.yeee.memo.integrate.thirdsdk.pay.biz;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import vip.yeee.memo.integrate.common.base.utils.JacksonUtils;
import vip.yeee.memo.integrate.common.model.exception.BizException;
import vip.yeee.memo.integrate.common.web.utils.HttpRequestUtils;
import vip.yeee.memo.integrate.common.web.utils.SpringContextUtils;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.OrderEnum;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.domain.mysql.entity.Order;
import vip.yeee.memo.integrate.thirdsdk.pay.domain.mysql.mapper.OrderMapper;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bizOrder.SubmitOrderReqVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bizOrder.SubmitOrderRespVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.*;
import vip.yeee.memo.integrate.thirdsdk.pay.properties.WxpayProperties;
import vip.yeee.memo.integrate.thirdsdk.pay.service.PayChannelService;
import vip.yeee.memo.integrate.thirdsdk.pay.utils.SeqKit;

import javax.annotation.Resource;
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
    private WxpayProperties wxpayProperties;

    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
    public SubmitOrderRespVO submitOrder(SubmitOrderReqVO reqVO) {
        log.info("【提交订单】- 请求参数：{}", reqVO);
        Order newOrder = new Order();
        newOrder.setCode(SeqKit.genOrderCode());
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

    public UnifiedOrderRespVO unifiedOrder(UnifiedOrderReqVO reqVO) throws Exception {
        log.info("【统一下单】- 请求参数：{}", reqVO);
        String[] waySplit = reqVO.getPayWay().split("_");
        if (waySplit.length < 2) {
            throw new BizException("支付方式CODE有误");
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
        reqBO.setTradeType(waySplit[1]);
        reqBO.setExpireTime(order.getExpiredTime());
        PayChannelService payChannelService = this.getPayChannelService(waySplit[0]);
        UnifiedOrderRespBO respBO = payChannelService.unifiedOrder(reqBO);
        UnifiedOrderRespVO orderRespVO = new UnifiedOrderRespVO(JacksonUtils.toJsonString(respBO));
        log.info("【统一下单】- 下单成功：{}", orderRespVO);
        // 更新订单状态
        Order upd = new Order();
        upd.setId(order.getId());
        upd.setState(OrderEnum.State.STATE_ING.getCode());
        orderMapper.updateById(upd);
        return orderRespVO;
    }

    private PayChannelService getPayChannelService(String payChannel) {
        String beanName;
        if (payChannel.equals(PayConstant.IF_CODE.WXPAY) && PayConstant.PAY_IF_VERSION.WX_V3.equals(wxpayProperties.getApiVersion())) {
            beanName = payChannel.toLowerCase() + "V3PayChannelService";
        } else {
            beanName = payChannel.toLowerCase() + "PayChannelService";
        }
        return (PayChannelService)SpringContextUtils.getBean(beanName);
    }

    private UnifiedOrderReqBO buildUnifiedOrderReqBO(UnifiedOrderReqVO reqVO) {
        String payWay = reqVO.getPayWay();
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

}
