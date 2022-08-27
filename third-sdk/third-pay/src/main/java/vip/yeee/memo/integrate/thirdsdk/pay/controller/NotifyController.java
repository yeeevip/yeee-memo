package vip.yeee.memo.integrate.thirdsdk.pay.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/26 14:10
 */
@RestController
public class NotifyController {

    @PostMapping("/wxpay/notify")
    public String wxpayNotifyHandler() {
        return null;
    }

    @PostMapping("/alipay/notify")
    public String alipayNotifyHandler() {
        return null;
    }

}
