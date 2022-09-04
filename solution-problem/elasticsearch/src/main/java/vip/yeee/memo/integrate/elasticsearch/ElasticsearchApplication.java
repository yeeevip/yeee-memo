package vip.yeee.memo.integrate.elasticsearch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/4/25 22:38
 */
@MapperScan("vip.yeee.memo.integrate.elasticsearch.opr.mapper")
@SpringBootApplication
public class ElasticsearchApplication {

    public static void main(String[] args) {
        new SpringApplication(ElasticsearchApplication.class).run(args);
    }

}
