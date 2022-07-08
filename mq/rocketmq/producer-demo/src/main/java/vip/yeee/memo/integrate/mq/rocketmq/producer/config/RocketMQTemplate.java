package vip.yeee.memo.integrate.mq.rocketmq.producer.config;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import vip.yeee.memo.integrate.mq.rocketmq.producer.model.MQConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * RocketMQ Template
 */
@RequiredArgsConstructor
@Component
public class RocketMQTemplate {

    private final ProducerBean producer;

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
        return this.producer.send(new Message(topic, tags, message.getBytes()));
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
