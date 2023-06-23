package vip.yeee.memo.demo.spring.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
        ApplicationContext context = new AnnotationConfigApplicationContext("vip.yeee.memo.demo.learn.spring.postprocessor");
        TestBean bean = (TestBean) context.getBean("testBean");
        log.info("bean = {}", bean);
    }

}
