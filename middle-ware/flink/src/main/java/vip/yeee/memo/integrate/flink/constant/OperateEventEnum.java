package vip.yeee.memo.integrate.flink.constant;

import lombok.Getter;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/6 16:16
 */
@Getter
public enum OperateEventEnum {

    VIEW_INDEX("访问首页"),
    VIEW_LIST("访问列表"),
    VIEW_DETAIL("访问详情"),

    ;

    private final String evnet;

    OperateEventEnum(String evnet) {
        this.evnet = evnet;
    }
}
