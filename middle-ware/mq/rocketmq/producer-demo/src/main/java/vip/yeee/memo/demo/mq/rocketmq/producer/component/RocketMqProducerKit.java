package vip.yeee.memo.demo.mq.rocketmq.producer.component;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import vip.yeee.memo.demo.mq.rocketmq.producer.config.MqConfig;
import vip.yeee.memo.demo.mq.rocketmq.producer.model.MQConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * RocketMQ Template
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RocketMqProducerKit {

    @Resource
    private MqConfig mqConfig;

    private final ProducerBean producer;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean buildProducer() {
        ProducerBean producer = new ProducerBean();
        producer.setProperties(mqConfig.getMqProperties());
        return producer;
    }

    /**
     * 普通发送
     * @param tags 过滤标签
     * @param message 消息内容
     * @return
     */
    public SendResult sendDefault(String tags, String message) {
        return this.producer.send(new Message(MQConstant.NORMAL_TOPIC, tags, message.getBytes()));
    }

    /**
     * 普通发送
     * @param tags 过滤标签
     * @param message 消息内容
     * @return
     */
    public SendResult sendDefault(String tags, String key, String message) {
        return this.producer.send(new Message(MQConstant.NORMAL_TOPIC, tags, key, message.getBytes()));
    }

    /**
     * 普通发送
     * @param topic 消息主题
     * @param tags 过滤标签
     * @param message 消息内容
     * @return
     */
    public SendResult send(String topic, String tags, String message) {
        log.info("RocketMq发送消息，topic = {}，tags = {}，message = {}", topic, tags, message);
        return this.producer.send(new Message(topic, tags, message.getBytes()));
    }

    /**
     * 发送延迟消息
     * @param topic 消息主题
     * @param tags 过滤标签
     * @param message 消息内容
     * @return
     */
    public SendResult sendDelayMsg(String topic, String tags, String message, long targetTime) {
        log.info("RocketMq发送消息，topic = {}，tags = {}，message = {}", topic, tags, message);
        Message msg = new Message(topic, tags, message.getBytes());
        msg.setStartDeliverTime(targetTime);
        return this.producer.send(msg);
    }

    /**
     * 普通发送
     * @param topic 消息主题
     * @param tags 过滤标签
     * @param key 消息key
     * @param message 消息内容
     * @return
     */
    public SendResult send(String topic, String tags, String key, String message) {
        return this.producer.send(new Message(topic, tags, key, message.getBytes()));
    }

    /**
     * 普通发送
     * @param topic 消息主题
     * @param message 消息内容
     * @return
     */
    public SendResult send(String topic, String message) {
        return this.producer.send(new Message(topic, "*", message.getBytes()));
    }

    /**
     * 单向消息
     * @param topic 消息主题
     * @param tags 过滤标签
     * @param message 消息内容
     */
    public void sendOneway(String topic, String tags, String message) {
        this.producer.sendOneway(new Message(topic, tags, message.getBytes()));
    }

    /**
     * 单向消息
     * @param topic 消息主题
     * @param message 消息内容
     */
    public void sendOneway(String topic, String message) {
        this.producer.sendOneway(new Message(topic, "*", message.getBytes()));
    }

    /**
     * 异步消息
     * @param topic 消息主题
     * @param tags 过滤标签
     * @param message 消息内容
     * @param sendCallback 回调
     */
    public void sendAsync(String topic, String tags, String message, SendCallback sendCallback) {
        this.producer.sendAsync(new Message(topic, tags, message.getBytes()), sendCallback);
    }

    /**
     * 异步消息
     * @param topic 消息主题
     * @param tags 过滤标签
     * @param key 消息key
     * @param message 消息内容
     * @param sendCallback 回调
     */
    public void sendAsync(String topic, String tags, String key, String message, SendCallback sendCallback) {
        this.producer.sendAsync(new Message(topic, tags, key, message.getBytes()), sendCallback);
    }

    /**
     * 异步消息
     * @param topic 消息主题
     * @param message 消息内容
     * @param sendCallback 回调
     */
    public void sendAsync(String topic, String message, SendCallback sendCallback) {
        this.producer.sendAsync(new Message(topic, "*", message.getBytes()), sendCallback);
    }

}
