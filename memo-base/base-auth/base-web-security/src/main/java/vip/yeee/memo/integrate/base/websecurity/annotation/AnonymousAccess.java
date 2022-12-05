package vip.yeee.memo.integrate.base.websecurity.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  用于标记匿名访问方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnonymousAccess {

    /**
     * 是否生效
     * @return
     */
    boolean valid() default true;

    /**
     * 是否签名
     * @return
     */
    boolean sign() default true;
}
