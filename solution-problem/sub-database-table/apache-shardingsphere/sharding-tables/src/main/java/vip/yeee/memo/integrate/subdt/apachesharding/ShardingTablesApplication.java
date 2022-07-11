package vip.yeee.memo.integrate.subdt.apachesharding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/6/22 15:14
 */
@MapperScan("vip.yeee.memo.integrate.subdt.apachesharding.mapper")
@SpringBootApplication
public class ShardingTablesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingTablesApplication.class, args);
    }

}
