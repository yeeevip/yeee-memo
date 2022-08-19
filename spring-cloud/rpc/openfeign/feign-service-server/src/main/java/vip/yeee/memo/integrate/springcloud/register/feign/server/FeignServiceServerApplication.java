package vip.yeee.memo.integrate.springcloud.register.feign.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan("vip.yeee.memo.integrate.springcloud.register.feign.server.domain.mysql.mapper")
@SpringBootApplication
@EnableDiscoveryClient
public class FeignServiceServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeignServiceServerApplication.class, args);
	}

}
