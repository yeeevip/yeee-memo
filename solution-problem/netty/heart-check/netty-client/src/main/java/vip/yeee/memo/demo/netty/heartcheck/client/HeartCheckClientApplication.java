package vip.yeee.memo.demo.netty.heartcheck.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import vip.yeee.memo.demo.netty.heartcheck.client.initializer.ClientStart;


/**
 *  服务器启动类
*/
@SpringBootApplication
public class HeartCheckClientApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(HeartCheckClientApplication.class, args);
		new ClientStart().initialize(context);
	}

}