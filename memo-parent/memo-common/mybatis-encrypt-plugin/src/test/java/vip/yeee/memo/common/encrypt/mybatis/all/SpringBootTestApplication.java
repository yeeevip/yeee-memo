package vip.yeee.memo.common.encrypt.mybatis.all;

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
