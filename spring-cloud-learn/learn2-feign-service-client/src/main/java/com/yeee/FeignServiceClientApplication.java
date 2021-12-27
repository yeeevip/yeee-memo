package com.yeee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableCircuitBreaker
@EnableFeignClients
public class FeignServiceClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeignServiceClientApplication.class, args);
	}

}
