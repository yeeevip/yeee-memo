package vip.yeee.memo.integrate.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.util.function.Consumer;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/13 15:10
 */
@Slf4j
@Component
public class KafkaProducer {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(String topic, String message) {
        log.info("【kafka发送消息】- 开始，topic = {}，message = {}", topic, message);
        kafkaTemplate.send(topic, message).addCallback(success -> {
            RecordMetadata recordMetadata = success.getRecordMetadata();
            log.info("【kafka发送消息】- 成功，topic = {}，partition = {}，offset = {}"
                    , recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
        }, failure -> log.info("【kafka发送消息】- 失败，message = {}", failure.getMessage()));
    }

    public void sendTransactionMessage(String topic, String message, Consumer<Void> anotherOpr) {
        log.info("【kafka发送消息】- 开始，topic = {}，message = {}", topic, message);
        kafkaTemplate.executeInTransaction(operations -> {
            ListenableFuture<SendResult<String, Object>> send = operations.send(topic, message);
            anotherOpr.accept(null);
            return send;
        }).addCallback(success -> {
            RecordMetadata recordMetadata = success.getRecordMetadata();
            log.info("【kafka发送消息】- 成功，topic = {}，partition = {}，offset = {}"
                    , recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
        }, failure -> log.info("【kafka发送消息】- 失败，message = {}", failure.getMessage()));
    }

}
