package com.learn.producer.demo.task;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.SendResult;
import com.learn.producer.demo.config.RocketMQTemplate;
import com.learn.producer.demo.model.MQConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/4/25 16:39
 */
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@Component
public class SendMQMessageTask {

    private final RocketMQTemplate rocketMQTemplate;

    @Scheduled(cron = "0/1 * * * * ?")
    private void testSendMsg() {
        SendResult sendResult = rocketMQTemplate.sendDefault(MQConstant.DEMO_MESSAGE_TAG, "测试一下发消息！！！测试一下发消息！！！测试一下发消息！！！测试一下发消息！！！");
        log.error("发送MQ消息成功，返回消息:{}", JSON.toJSONString(sendResult));
    }

}
