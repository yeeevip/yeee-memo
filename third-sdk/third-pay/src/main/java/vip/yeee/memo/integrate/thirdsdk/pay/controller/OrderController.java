package vip.yeee.memo.integrate.thirdsdk.pay.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.yeee.memo.integrate.base.model.rest.CommonResult;
import vip.yeee.memo.integrate.base.util.CodeImgUtil;
import vip.yeee.memo.integrate.thirdsdk.pay.biz.OrderBiz;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.req.OrderQueryReqVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.req.SubmitOrderReqVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.req.UnifiedOrderRefundReqVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.resp.CheckPayStateResVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.resp.SubmitOrderRespVO;
import vip.yeee.memo.integrate.thirdsdk.pay.model.vo.req.UnifiedOrderReqVO;
import vip.yeee.memo.integrate.thirdsdk.pay.paykit.PayKit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
     * 统一下单/创建三方[预支付订单]
     */
    @PostMapping("/unified/order/create")
    public CommonResult<Object> unifiedOrder(@Validated @RequestBody UnifiedOrderReqVO reqVO) throws Exception {
        return CommonResult.success(orderBiz.unifiedOrder(reqVO));
    }

    /**
     * 统一下单/订单退款
     */
    @PostMapping("/unified/order/refund")
    public CommonResult<Object> unifiedOrderRefund(@Validated @RequestBody UnifiedOrderRefundReqVO reqVO) {
        return CommonResult.success(orderBiz.unifiedOrderRefund(reqVO));
    }

    /**
     * 同步查询支付结果
     * 支付完成后，返回商户页面可以调用
     */
    @PostMapping({"/biz/order/query"})
    public CommonResult<CheckPayStateResVO> checkOrderQuery(@RequestBody OrderQueryReqVO request) {
        return CommonResult.success(orderBiz.checkOrderQuery(request));
    }

    /**
     * 三方支付回调处理
     */
    @PostMapping({"/api/pay/notify/{ifCode}/{lesseeId}"})
    public ResponseEntity<Object> handlePayNotify(HttpServletRequest request, @PathVariable("ifCode") String ifCode, @PathVariable("lesseeId") String lesseeId) throws Exception {
        return orderBiz.handlePayNotify(request, ifCode, lesseeId);
    }

    /**
     * 三方退款回调处理
     */
    @PostMapping({"/api/refund/notify/{ifCode}/{lesseeId}"})
    public ResponseEntity<Object> handleRefundNotify(HttpServletRequest request, @PathVariable("ifCode") String ifCode, @PathVariable("lesseeId") String lesseeId) throws Exception {
        return orderBiz.handleRefundNotify(request, ifCode, lesseeId);
    }

    @RequestMapping("/general/img/qr/{aesStr}.png")
    public void genQrImg(HttpServletResponse response, @PathVariable("aesStr") String aesStr) throws Exception {
        int width = 200, height = 200;
        CodeImgUtil.writeQrCode(response.getOutputStream(), PayKit.aes.decryptStr(aesStr), width, height);
    }

}
