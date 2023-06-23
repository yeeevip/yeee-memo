package vip.yeee.memo.demo.mq.rocketmq.producer.component;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.OrderProducerBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import vip.yeee.memo.demo.mq.rocketmq.producer.config.MqConfig;

import javax.annotation.Resource;

/**
 * RocketMQ Template
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RocketMqOrderProducerKit {

    @Resource
    private MqConfig mqConfig;

    private final OrderProducerBean orderProducer;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public OrderProducerBean buildProducer() {
        OrderProducerBean producer = new OrderProducerBean();
        producer.setProperties(mqConfig.getMqProperties());
        return producer;
    }

    /**
     * 指定队列发送/顺序消息
     * @param topic 消息主题
     * @param tags 过滤标签
     * @param message 消息内容
     * @return
     */
    public SendResult send(String topic, String tags, String message, Object shardingKey) {
        log.info("RocketMq发送顺序消息，topic = {}，tags = {}，message = {}", topic, tags, message);
        return this.orderProducer.send(new Message(topic, tags, message.getBytes()), shardingKey.toString());
    }

}
