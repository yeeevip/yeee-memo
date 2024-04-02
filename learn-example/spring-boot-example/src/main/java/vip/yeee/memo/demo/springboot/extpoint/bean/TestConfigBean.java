package vip.yeee.memo.demo.springboot.extpoint.bean;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/8/14 9:51
 */
@Configuration
@Data
public class TestConfigBean {

    @Bean(initMethod = "init")
    public TestBean03 testBean03() {
        return new TestBean03();
    }
}