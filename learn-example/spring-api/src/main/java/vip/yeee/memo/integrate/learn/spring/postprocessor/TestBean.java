package vip.yeee.memo.integrate.learn.spring.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/28 9:30
 */
@Component
public class TestBean {

    @Resource
    private TestBean1 testBean1;
    @Resource
    private TestBean2 testBean2;

    public void testMethod() {
        testBean1.testMethod();
        testBean2.testMethod();
    }

    public void setTestBean1(TestBean1 testBean1) {
        this.testBean1 = testBean1;
    }

    public void setTestBean2(TestBean2 testBean2) {
        this.testBean2 = testBean2;
    }
}
@Slf4j
@Component
class TestBean1 {
    public void testMethod() {
        log.info("TestBean1 - testMethod");
    }
}
@Slf4j
@Component
class TestBean2 {
    public void testMethod() {
        log.info("TestBean2 - testMethod");
    }
}
