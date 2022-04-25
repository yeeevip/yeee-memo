package com.learn.producer.demo.config;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Setter
@Getter
@Configuration
public class MqConfig {

    @Value("${rocketmq.accessKey}")
    private String accessKey;
    @Value("${rocketmq.secretKey}")
    private String secretKey;
    @Value("${rocketmq.nameSrvAddr}")
    private String nameSrvAddr;

    //@Value("${rocketmq.topic}")
    private String topic;
    //@Value("${rocketmq.groupId}")
    private String groupId;
    //@Value("${rocketmq.tag}")
    private String tag;
    //@Value("${rocketmq.orderTopic}")
    private String orderTopic;
    //@Value("${rocketmq.orderGroupId}")
    private String orderGroupId;
    //@Value("${rocketmq.orderTag}")
    private String orderTag;

    public Properties getMqProperties() {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, this.accessKey);
        properties.setProperty(PropertyKeyConst.SecretKey, this.secretKey);
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, this.nameSrvAddr);
        return properties;
    }

}
