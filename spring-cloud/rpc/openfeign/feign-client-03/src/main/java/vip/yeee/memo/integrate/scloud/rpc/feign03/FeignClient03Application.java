package vip.yeee.memo.integrate.scloud.rpc.feign03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/3 15:11
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class FeignClient03Application {

    public static void main(String[] args) {
        SpringApplication.run(FeignClient03Application.class, args);
    }

}
