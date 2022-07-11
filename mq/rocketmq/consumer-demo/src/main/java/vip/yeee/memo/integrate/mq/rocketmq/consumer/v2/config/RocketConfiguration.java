package vip.yeee.memo.integrate.mq.rocketmq.consumer.v2.config;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.yeee.memo.integrate.mq.rocketmq.consumer.v1.config.MqConfig;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Properties;

@Slf4j
@Configuration
public class RocketConfiguration {

    @Resource
    private MqConfig mqConfig;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "rocketmq.consumer.enabled", havingValue = "true")
    public ConsumerBean buildConsumer() {
        log.info("# start initializing rocket CONSUMER, AK={}, GROUP_ID={}, CONSUME_THREAD_NUMS={}",
                mqConfig.getAccessKey(), mqConfig.getConsumer().getGroupId(),
                mqConfig.getConsumer().getConsumeThreadNums());
        ConsumerBean consumerBean = new ConsumerBean();
        Properties properties = mqConfig.getMqProperties();
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, mqConfig.getConsumer().getConsumeThreadNums() + "");
        properties.setProperty(PropertyKeyConst.GROUP_ID, mqConfig.getConsumer().getGroupId());
        consumerBean.setProperties(properties);
        consumerBean.setSubscriptionTable(new HashMap<>(0));
        log.info("# rocket CONSUMER initialized successfully");
        return consumerBean;
    }

}
