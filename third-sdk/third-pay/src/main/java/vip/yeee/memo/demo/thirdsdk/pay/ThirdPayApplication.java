package vip.yeee.memo.demo.thirdsdk.pay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/8/19 16:37
 */
@MapperScan(basePackages = "vip.yeee.memo.demo.thirdsdk.pay.domain.mysql.mapper")
@SpringBootApplication
public class ThirdPayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThirdPayApplication.class, args);
    }

}
