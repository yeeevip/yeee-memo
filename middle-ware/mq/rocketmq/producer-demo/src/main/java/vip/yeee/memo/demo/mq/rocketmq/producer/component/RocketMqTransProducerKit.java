package vip.yeee.memo.demo.mq.rocketmq.producer.component;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.TransactionProducerBean;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import vip.yeee.memo.demo.mq.rocketmq.producer.config.MqConfig;
import vip.yeee.memo.demo.mq.rocketmq.producer.service.IBusinessService;

import javax.annotation.Resource;

/**
 * RocketMQ Template
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RocketMqTransProducerKit {

    @Resource
    private MqConfig mqConfig;

    private final TransactionProducerBean transactionProducerBean;

    private IBusinessService businessService;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public TransactionProducerBean buildProducer() {
        TransactionProducerBean producer = new TransactionProducerBean();
        producer.setProperties(mqConfig.getMqProperties());
        producer.setLocalTransactionChecker(msg -> {
            TransactionStatus transactionStatus = TransactionStatus.Unknow;
            try {
                log.info("RocketMq检查本地事务状态，msgId = {}，topic = {}，tag = {}", msg.getMsgID(), msg.getTopic(), msg.getTag());
                boolean success = businessService.checkService(msg);
                if (success) {
                    transactionStatus = TransactionStatus.CommitTransaction;
                } else {
                    transactionStatus = TransactionStatus.RollbackTransaction;
                }
            } catch (Exception e) {
                log.info("RocketMq检查本地事务状态异常，msgId = {}，topic = {}，tag = {}", msg.getMsgID(), msg.getTopic(), msg.getTag(), e);
            }
            return transactionStatus;
        });
        return producer;
    }

    /**
     * 发送事务消息
     * @param topic 消息主题
     * @param tags 过滤标签
     * @param message 消息内容
     * @return
     */
    public SendResult send(String topic, String tags, String message) {
        log.info("RocketMq发送事务消息，topic = {}，tags = {}，message = {}", topic, tags, message);
        return this.transactionProducerBean.send(new Message(topic, tags, message.getBytes()), (msg, arg) -> {
            TransactionStatus transactionStatus = TransactionStatus.Unknow;
            try {
                boolean success = businessService.executeService(msg);
                if (!success) {
                    log.info("RocketMq发送事务消息，本地消息未成功");
                    transactionStatus = TransactionStatus.RollbackTransaction;
                } else {
                    transactionStatus = TransactionStatus.CommitTransaction;
                }
            } catch (Exception e) {
                log.info("RocketMq发送事务消息，本地消息执行异常", e);
            }
            return transactionStatus;
        }, null);
    }

}
