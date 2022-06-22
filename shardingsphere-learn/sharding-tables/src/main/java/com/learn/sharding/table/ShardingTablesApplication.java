package com.learn.sharding.table;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/6/22 15:14
 */
@MapperScan("com.learn.sharding.table.mapper")
@SpringBootApplication
public class ShardingTablesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingTablesApplication.class, args);
    }

}
