package vip.yeee.memo.demo.springboot.extpoint.bean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/8/14 14:03
 */
@Slf4j
@Data
public class TestBean03 implements InitializingBean {

    private TestBean04 testBean04;

    @Autowired
    public void setTestBean03(TestBean04 testBean04) {
        this.testBean04 = testBean04;
        log.info("【Bean初始化】--------TestBean03注入属性testBean04----------");
    }

    public TestBean03() {
        log.info("【Bean初始化】--------TestBean03实例化----------");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("【Bean初始化】--------InitializingBean.afterPropertiesSet ----------");
    }

    public void init() {
        log.info("【Bean初始化】--------自定义方法 ----------");
    }

    @PostConstruct
    public void postConstruct() {
        log.info("【Bean初始化】--------@PostConstruct ----------");
    }
}
