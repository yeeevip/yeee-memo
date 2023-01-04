package vip.yeee.memo.integrate.learn.spring.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/27 17:49
 */
@Slf4j
//@ExtendWith({SpringExtension.class})
public class PostProcessorTests {

    @Test
    public void test1() {
//        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-application.xml");
        ApplicationContext context = new AnnotationConfigApplicationContext("vip.yeee.memo.integrate.learn.spring.postprocessor");
        TestBean bean = (TestBean) context.getBean("testBean");
        log.info("bean = {}", bean);
    }

}
