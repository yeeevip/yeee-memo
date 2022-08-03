package vip.yeee.memo.integrate.springcloud.register.feign.fallback.sentinel;

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
public class FeignCallFallbackSentinelApp {

    public static void main(String[] args) {
        SpringApplication.run(FeignCallFallbackSentinelApp.class, args);
    }

}
