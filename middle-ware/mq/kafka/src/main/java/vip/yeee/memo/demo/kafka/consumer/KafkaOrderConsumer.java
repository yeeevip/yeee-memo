package vip.yeee.memo.demo.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import vip.yeee.memo.base.util.JacksonUtils;
import vip.yeee.memo.demo.kafka.model.qo.OrderQo;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class KafkaOrderConsumer {

    //队列数量，根据业务情况决定
    //本案例，队列=4，线程=4，一一对应
    ExecutorService[] queues = new ExecutorService[4];

    @PostConstruct
    public void init() {
        //遍历队列数组，初始化每一个元素，同时让线程启动
        for (int i = 0; i < 4; i++) {
            log.info("thread started , index = {}", i);
            queues[i] = Executors.newSingleThreadExecutor();
        }
    }

    /**
     * 单线程消费，消费能力弱，offset容易控制提交
     */
    @KafkaListener(topics = {"sorted"}, topicPattern = "0", groupId = "order-consumer1")
    public void handleOrderMessage(ConsumerRecord<?, ?> record) {
        log.info("【顺序消费监听】，topic = {}，partition = {}，offset = {}, value = {}"
                , record.topic(), record.partition(), record.offset(), record.value());
    }

    /**
     * 多线程消费，消费能力强，offset不好控制，需要考虑重复消费的问题
     */
    @KafkaListener(topics = {"sorted"}, topicPattern = "0", groupId = "order-consumer1")
    public void handleOrderMessage2(ConsumerRecord<?, ?> record) {
        log.info("【顺序消费监听】，topic = {}，partition = {}，offset = {}, value = {}"
                , record.topic(), record.partition(), record.offset(), record.value());
    }

    @KafkaListener(topics = {"sorted"}, topicPattern = "0", groupId = "sorted-group-1")
    public void onMessage(ConsumerRecord<?, ?> consumerRecord) throws Exception {
        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
        if (optional.isPresent()) {
            Object msg = optional.get();
//            从kafka里获取到消息
//            注意分发方式，kafka有两个分区0和1，对应两个消费者
//            当前分区是0，也就是偶数的id，0、2、4、6会在这里被消费
            OrderQo order = JacksonUtils.toJavaBean(String.valueOf(msg), OrderQo.class);

//            而队列是4个。也就是每个消费者再分到两个队列里去
//            队列另一端分别对应4个线程在等待
//            所以，按4取余数
            int index = order.getId() % 4;
//            logger.info("put to queue, queue={}, order:[id={}, status={}]", index, order.getId(), order.getStatus());

            queues[index].execute(() -> {
                log.info("get from queue, queue:{}, order:[id={}, status={}]", index, order.getId(), order.getStatus());
            });
        }
    }

    @KafkaListener(topics = {"sorted"}, topicPattern = "1", groupId = "sorted-group-1")
    public void onMessage1(ConsumerRecord<?, ?> consumerRecord) throws Exception {
        //相同的实现，现实中为另一台机器，这里用两个listener模拟
        //奇数的id会被分到这里，也就是1、3、5、7
        this.onMessage(consumerRecord);
    }

}
