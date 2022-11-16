package vip.yeee.memo.integrate.common.websecurity.constant;

import lombok.Getter;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/16 15:03
 */
@Getter
public enum SecurityUserTypeEnum {

    SYSTEM_USER("SYSTEM", "SYSTEM-USER"),
    FRONT_USER("FRONT", "FRONT-USER")
    ;

    private final String type;
    private final String role;

    SecurityUserTypeEnum(String type, String role) {
        this.type = type;
        this.role = role;
    }
}
