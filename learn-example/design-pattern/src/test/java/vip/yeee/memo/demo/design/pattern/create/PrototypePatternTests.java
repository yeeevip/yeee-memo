package vip.yeee.memo.demo.design.pattern.create;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/7/21 18:07
 */
@Slf4j
public class PrototypePatternTests {

    @Test
    public void test() throws CloneNotSupportedException {
        ConcreteInstance ori = new ConcreteInstance();
        log.info("ori = {}", ori);
        ConcreteInstance cur = ori.clone();
        log.info("cur = {}", cur);
    }

}
