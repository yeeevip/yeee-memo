package vip.yeee.memo.integrate.common.mep.all;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SpringBootTestApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringBootTestApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
