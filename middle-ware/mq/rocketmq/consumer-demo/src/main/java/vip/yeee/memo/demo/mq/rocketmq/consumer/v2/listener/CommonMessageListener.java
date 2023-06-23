package vip.yeee.memo.demo.mq.rocketmq.consumer.v2.listener;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.demo.mq.rocketmq.consumer.v1.config.MqConfig;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/7/11 11:17
 */
@Slf4j
@Component
public class CommonMessageListener implements MessageListener {

    @Resource
    private MqConfig mqConfig;

    @Resource
    private ConsumerBean consumerBean;

    @PostConstruct
    public void subscribe() {
        consumerBean.subscribe(mqConfig.getTopic().getDemoTopic1(), "*", this);
        consumerBean.subscribe(mqConfig.getTopic().getDemoTopic2(), "*", this);
    }

    @Override
    public Action consume(Message message, ConsumeContext context) {
        log.info("\n \nDemo Receive: {} \n \n" , new String (message.getBody()));
        try {
            if ("topicName".equals(message.getTopic()) && "tagName".equals(message.getTag())) {
                //do something..
            }
            return Action.CommitMessage;
        } catch (Exception e) {
            //消费失败
            return Action.ReconsumeLater;
        }
    }

}
