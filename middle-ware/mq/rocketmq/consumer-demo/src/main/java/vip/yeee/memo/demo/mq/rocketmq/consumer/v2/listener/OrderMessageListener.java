package vip.yeee.memo.demo.mq.rocketmq.consumer.v2.listener;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.bean.OrderConsumerBean;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.demo.mq.rocketmq.consumer.v1.config.MqConfig;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/7/11 11:17
 */
@Slf4j
@Component
public class OrderMessageListener implements MessageOrderListener {

    @Resource
    private MqConfig mqConfig;

    @Resource
    private OrderConsumerBean orderConsumerBean;

    @PostConstruct
    public void subscribe() {
        orderConsumerBean.subscribe(mqConfig.getTopic().getDemoTopic1(), "*", this);
        orderConsumerBean.subscribe(mqConfig.getTopic().getDemoTopic2(), "*", this);
    }

    @Override
    public OrderAction consume(Message message, ConsumeOrderContext context) {
        log.info("\n \nDemo Receive: {} \n \n" , new String (message.getBody()));
        try {
            if ("topicName".equals(message.getTopic()) && "tagName".equals(message.getTag())) {
                //do something..
            }
            return OrderAction.Success;
        } catch (Exception e) {
            //消费失败
            return OrderAction.Suspend;
        }
    }
}
