package vip.yeee.memo.demo.thirdsdk.pay.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/20 18:18
 */
public class OrderEnum {

    @Getter
    public static enum PayState {

        INIT(0, "订单生成"),
        ING(10, "支付中"),
        SUCCESS(20, "支付成功"),
        FAIL(30, "支付失败"),
        CANCEL(40, "已撤销"),
        REFUND_FAIL(41, "退款失败"),
        REFUND(50, "已退款"),
        CLOSED(60, "订单关闭"),
        ;

        private final Integer code;
        private final String desc;

        PayState(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static boolean checkEndState(Integer state) {
            if (state == null) {
                return true;
            }
            return Arrays.asList(SUCCESS.getCode(), CANCEL.getCode(), REFUND.getCode(), CLOSED.getCode())
                    .contains(state);
        }

        public static String getDescByCode(Integer code) {
            Optional<PayState> optional = Arrays.stream(values()).filter(item -> item.code.equals(code)).findFirst();
            return optional.map(state -> state.desc).orElse(null);
        }

    }

    @Getter
    public static enum RefundState {

        INIT(0, "订单生成"),
        ING(10, "退款中"),
        SUCCESS(20, "退款成功"),
        FAIL(30, "退款失败"),
        CANCEL(40, "退款任务关闭")
        ;
        private final Integer code;
        private final String desc;

        RefundState(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

    }

}
