package vip.yeee.memo.common.encrypt.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 标记在DTO类上，用于说明是否支持加解密
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SensitiveEncryptEnabled {
    /**
     * 是否开启加解密和脱敏模式
     * @return SensitiveEncryptEnabled
     */
    boolean value() default true;
}
