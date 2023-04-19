package vip.yeee.memo.integrate.nio.webserver.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 注解，用于自定义访问的URL路径<br>
 * 值可以是一个请求路径，如果需要指定HTTP方法，在前面加方法名用":"分隔既可<br>
 * @author loolly
 *
 */
@Retention(RetentionPolicy.RUNTIME)/*保留的时间长短*/
@Inherited/*只用于class，可被子类继承*/
public @interface Route {
	String value();
}
