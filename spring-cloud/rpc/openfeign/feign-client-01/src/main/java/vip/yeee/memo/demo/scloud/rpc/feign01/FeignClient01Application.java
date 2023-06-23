package vip.yeee.memo.demo.scloud.rpc.feign01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FeignClient01Application {

	public static void main(String[] args) {
		SpringApplication.run(FeignClient01Application.class, args);
	}

}
