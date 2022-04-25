package com.learn.consumer.demo.listener;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.learn.consumer.demo.annotation.RocketMQMessageListener;
import com.learn.consumer.demo.model.MQConstant;
import lombok.extern.slf4j.Slf4j;


/**
 * MQ 测试Listener
 */
@Slf4j
@RocketMQMessageListener(topic = MQConstant.NORMAL_TOPIC, consumerGroup = MQConstant.DEFAULT_GROUP, selectorExpression = MQConstant.DEMO_MESSAGE_TAG/*, consumeMode = ConsumeMode.CLUSTERING*/)
public class DemoMessageListener implements MessageListener {

    @Override
    public Action consume(Message message, ConsumeContext context) {

        log.info("\n \nDemo Receive: {} \n \n" , new String (message.getBody()));
        try {
            //do something..
            return Action.CommitMessage;
        } catch (Exception e) {
            //消费失败
            return Action.ReconsumeLater;
        }
    }
}
