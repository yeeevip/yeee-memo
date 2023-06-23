package vip.yeee.memo.demo.mq.rocketmq.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/4/25 15:17
 */
@SpringBootApplication
public class ConsumerDemoApplication {

    public static void main(String[] args) {
        new SpringApplication(ConsumerDemoApplication.class).run(args);
    }

}
