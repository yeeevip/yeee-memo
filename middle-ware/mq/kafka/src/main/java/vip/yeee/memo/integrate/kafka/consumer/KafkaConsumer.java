package vip.yeee.memo.integrate.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/13 15:20
 */
@Slf4j
@Component
public class KafkaConsumer {

    /**
     * 点对点模式消费
     */
    @KafkaListener(topics = {"TP_memo-test"})
    public void listener1(ConsumerRecord<String, Object> record, Acknowledgment ack) {
        try {
            log.info("【消费监听1】，topic = {}，partition = {}，offset = {}, value = {}"
                    , record.topic(), record.partition(), record.offset(), record.value());
//             throw new Exception();
            // 使侦听器可以控制何时提交偏移量
            ack.acknowledge();
        } catch (Exception e) {
            log.info("【消费监听1】- 消费异常，topic = {}，partition = {}，offset = {}, value = {}"
                    , record.topic(), record.partition(), record.offset(), record.value());
//            ack.acknowledge();
            // 从上一次 poll() 拉取到的，所有正在处理的偏移量将被提交，其余的将被丢弃，本次处理失败和未处理的记录，将在下一次传递
            ack.nack(TimeUnit.SECONDS.toMillis(10));
        }
    }

    /**
     * 发布订阅模式消费/groupId不同
     */
    @KafkaListener(topics = {"TP_memo-test"}, groupId = "22222222222222222222")
    public void listener2(ConsumerRecord<?, ?> record) {
        log.info("【消费监听2】，topic = {}，partition = {}，offset = {}, value = {}"
                , record.topic(), record.partition(), record.offset(), record.value());
    }

    @KafkaListener(topics = {"TP_canal-yeeevip"})
    public void listenerCanalMessage(ConsumerRecord<?, ?> record, Acknowledgment ack) {
        log.info("【消费监听2】，topic = {}，partition = {}，offset = {}, value = {}"
                , record.topic(), record.partition(), record.offset(), record.value());
        ack.acknowledge();
    }

}
