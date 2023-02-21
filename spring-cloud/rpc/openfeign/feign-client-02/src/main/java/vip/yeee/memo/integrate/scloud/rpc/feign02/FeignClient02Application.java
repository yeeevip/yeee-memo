package vip.yeee.memo.integrate.scloud.rpc.feign02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableCircuitBreaker
@EnableDiscoveryClient
@EnableFeignClients
public class FeignClient02Application {

	public static void main(String[] args) {
		SpringApplication.run(FeignClient02Application.class, args);
	}

}
