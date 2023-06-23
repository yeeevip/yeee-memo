package vip.yeee.memo.demo.springboot.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/29 10:00
 */
public class SpringBootWebTest {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ExampleWebConfig.class);
        application.setWebApplicationType(WebApplicationType.SERVLET);
        application.run();
    }

}

//@SpringBootApplication
@Configuration(proxyBeanMethods = false)
class ExampleWebConfig {

    @Bean
    TomcatServletWebServerFactory webServer() {
        return new TomcatServletWebServerFactory(8080);
    }

}
