package vip.yeee.memo.integrate.springcloud.register.feign.client;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("vip.yeee.memo.integrate.springcloud.register.feign.client.domain.mysql.mapper")
@SpringBootApplication
//@EnableCircuitBreaker
@EnableDiscoveryClient
@EnableFeignClients
public class FeignServiceClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeignServiceClientApplication.class, args);
	}

}
