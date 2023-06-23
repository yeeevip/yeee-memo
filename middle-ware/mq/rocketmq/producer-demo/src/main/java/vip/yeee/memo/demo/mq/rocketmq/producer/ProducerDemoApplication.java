package vip.yeee.memo.demo.mq.rocketmq.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/4/25 15:23
 */
@SpringBootApplication
public class ProducerDemoApplication {

    public static void main(String[] args) {
        new SpringApplication(ProducerDemoApplication.class).run(args);
    }

}
