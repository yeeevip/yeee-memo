package vip.yeee.memo.integrate.seata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/18 16:48
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class SeataClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataClientApplication.class, args);
    }

}
