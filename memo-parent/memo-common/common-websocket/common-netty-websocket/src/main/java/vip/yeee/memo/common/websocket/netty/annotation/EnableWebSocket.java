package vip.yeee.memo.common.websocket.netty.annotation;

import org.springframework.context.annotation.Import;
import vip.yeee.memo.common.websocket.netty.config.NettyWebSocketConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(NettyWebSocketConfiguration.class)
public @interface EnableWebSocket {

    String[] scanBasePackages() default {};

}
