package vip.yeee.memo.demo.scloud.tac.seata03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/18 16:48
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class SeataClient03Application {

    public static void main(String[] args) {
        SpringApplication.run(SeataClient03Application.class, args);
    }

}
