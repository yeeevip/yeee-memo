package vip.yeee.memo.integrate.thirdsdk.pay.constant;

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
    public static enum State {

        STATE_INIT(0, "订单生成"),
        STATE_ING(10, "支付中"),
        STATE_SUCCESS(20, "支付成功"),
        STATE_FAIL(30, "支付失败"),
        STATE_CANCEL(40, "已撤销"),
        STATE_REFUND(50, "已退款"),
        STATE_CLOSED(60, "订单关闭"),
        ;

        private final Integer code;
        private final String desc;

        State(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static boolean checkEndState(Integer state) {
            if (state == null) {
                return true;
            }
            return Arrays.asList(STATE_SUCCESS.getCode(), STATE_CANCEL.getCode(), STATE_REFUND.getCode(), STATE_CLOSED.getCode())
                    .contains(state);
        }

        public static String getDescByCode(Integer code) {
            Optional<State> optional = Arrays.stream(values()).filter(item -> item.code.equals(code)).findFirst();
            return optional.map(state -> state.desc).orElse(null);
        }

    }

    @Getter
    public static enum Refund {

        REFUND_STATE_NONE(0, "未发生实际退款"),
        REFUND_STATE_SUB(10, "部分退款"),
        REFUND_STATE_ALL(20, "全额退款"),
        ;

        private final Integer code;
        private final String desc;

        Refund(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

    }

}
