package vip.yeee.memo.demo.mq.rocketmq.consumer.v1.config;

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

    private Consumer consumer;

    private Topic topic;

    @Getter
    @Setter
    public static class Consumer {
        private Boolean enabled = Boolean.FALSE;
        private Integer consumeThreadNums = 20;
        private String groupId = "default";
    }

    @Getter
    @Setter
    public static class Topic {
        private String demoTopic1;
        private String demoTopic2;
    }

    public Properties getMqProperties() {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, this.accessKey);
        properties.setProperty(PropertyKeyConst.SecretKey, this.secretKey);
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, this.nameSrvAddr);
        return properties;
    }

}
