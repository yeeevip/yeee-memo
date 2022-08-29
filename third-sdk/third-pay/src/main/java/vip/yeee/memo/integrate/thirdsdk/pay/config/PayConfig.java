package vip.yeee.memo.integrate.thirdsdk.pay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.yeee.memo.integrate.thirdsdk.pay.constant.PayConstant;
import vip.yeee.memo.integrate.thirdsdk.pay.properties.PayProperties;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 17:32
 */
@Configuration
public class PayConfig {

    @Resource
    private PayProperties payProperties;

    @Bean
    public WxPayService wxPayService() {
        WxPayService wxPayService = new WxPayServiceImpl();
        WxPayConfig wxPayConfig = new WxPayConfig();
        BeanUtils.copyProperties(payProperties.getWx(), wxPayConfig);
        if (PayConstant.PAY_IF_VERSION.WX_V2.equals(payProperties.getWx().getApiVersion())) {
            wxPayConfig.setSignType(WxPayConstants.SignType.MD5);
        }
        wxPayService.setConfig(wxPayConfig);
        return wxPayService;
    }

    @Bean
    public AlipayClient alipayClient() {
        PayProperties.AlipayProperties ali = payProperties.getAli();
        return new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do"
                , ali.getAppId(), ali.getPrivateKey(), "json", "UTF-8",
                ali.getAlipayPublicKey(), ali.getSignType());
    }

}