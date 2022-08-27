package vip.yeee.memo.integrate.thirdsdk.pay.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vip.yeee.memo.integrate.common.model.rest.CommonResult;
import vip.yeee.memo.integrate.thirdsdk.pay.biz.OrderBiz;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bizOrder.SubmitOrderReqVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.bizOrder.SubmitOrderRespVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.UnifiedOrderReqVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.unifiedOrder.UnifiedOrderRespVO;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 14:41
 */
@RestController
public class OrderController {

    @Resource
    private OrderBiz orderBiz;

    /**
     * 提交/创建本地订单
     */
    @PostMapping("/biz/order/submit")
    public CommonResult<SubmitOrderRespVO> submitOrder(@Validated @RequestBody SubmitOrderReqVO reqVO) {
        return CommonResult.success(orderBiz.submitOrder(reqVO));
    }

    /**
     * 统一下单/创建三方预支付订单
     */
    @PostMapping("/unified/order/create")
    public CommonResult<UnifiedOrderRespVO> unifiedOrder(@Validated @RequestBody UnifiedOrderReqVO reqVO) throws Exception {
        return CommonResult.success(orderBiz.unifiedOrder(reqVO));
    }

}
