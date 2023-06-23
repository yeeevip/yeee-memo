package vip.yeee.memo.demo.kafka.handler;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

/**
 *  kafka回调
 **/

@Component
public class KafkaSendResultHandler implements ProducerListener<String, Object> {

    private static final Logger logger = LoggerFactory.getLogger(KafkaSendResultHandler.class);

    @Override
    public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {
        logger.info("Message send success : " + producerRecord.toString());
    }

    @Override
    public void onError(ProducerRecord producerRecord, RecordMetadata recordMetadata, Exception exception) {
        logger.error("Message send error : " + producerRecord.toString());
        logger.error("occurred exception",exception);
    }
}
