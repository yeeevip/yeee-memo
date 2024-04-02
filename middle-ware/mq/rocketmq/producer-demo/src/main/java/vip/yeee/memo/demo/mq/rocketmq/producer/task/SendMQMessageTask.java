package vip.yeee.memo.demo.mq.rocketmq.producer.task;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.SendResult;
import vip.yeee.memo.demo.mq.rocketmq.producer.component.RocketMqProducerKit;
import vip.yeee.memo.demo.mq.rocketmq.producer.model.MQConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/4/25 16:39
 */
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@Component
public class SendMQMessageTask {

    private final RocketMqProducerKit rocketMqProducerKit;

    @Scheduled(cron = "0/1 * * * * ?")
    private void testSendMsg() {
        SendResult sendResult = rocketMqProducerKit.sendDefault(MQConstant.DEMO_MESSAGE_TAG, "测试一下发消息！！！测试一下发消息！！！测试一下发消息！！！测试一下发消息！！！");
        log.error("发送MQ消息成功，返回消息:{}", JSON.toJSONString(sendResult));
    }

}
