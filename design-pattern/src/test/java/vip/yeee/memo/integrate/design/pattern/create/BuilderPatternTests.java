package vip.yeee.memo.integrate.design.pattern.create;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/7/21 17:57
 */
@Slf4j
public class BuilderPatternTests {

    @Test
    public void test() {
        User user = new User.Builder()
                .id("1111")
                .name("1111")
                .password("1111")
                .phone("1111")
                .build();
        log.info("user = {}", user);
    }

}
