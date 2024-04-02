package vip.yeee.memo.demo.scloud.tac.seata02;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/18 16:48
 */
@MapperScan("vip.yeee.memo.demo.scloud.tac.seata02.domain.mysql.mapper")
@SpringBootApplication
@EnableDiscoveryClient
public class SeataClient02Application {

    public static void main(String[] args) {
        SpringApplication.run(SeataClient02Application.class, args);
    }

}
