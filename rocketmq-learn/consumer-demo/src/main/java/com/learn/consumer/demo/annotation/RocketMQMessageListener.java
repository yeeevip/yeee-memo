package com.learn.consumer.demo.annotation;

import com.learn.consumer.demo.model.ConsumeMode;
import com.learn.consumer.demo.model.MessageType;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RocketMQMessageListener {

    /**
     * 消费组
     * @return
     */
    String consumerGroup();

    /**
     * 主题
     * @return
     */
    String topic();

    /**
     * 过滤表达式
     * @return
     */
    String selectorExpression() default "*";

    /**
     * 集群订阅方式设置（不设置的情况下，默认为集群订阅方式）。
     * @return
     */
    ConsumeMode consumeMode() default ConsumeMode.CLUSTERING;

    /**
     * 消息类型
     * @return
     */
    MessageType type() default MessageType.NORMAL;
}
