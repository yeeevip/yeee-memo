package vip.yeee.memo.integrate.design.pattern.create;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/7/21 11:31
 */
@Slf4j
public class SingletonPatternTests {

    @Test
    public void test() {
        InitSingleton instance = InitSingleton.getInstance();
        log.info("instance1 = {}", instance);
        LazySingleton instance2 = LazySingleton.getInstance();
        log.info("instance2 = {}", instance2);
    }

}
