package vip.yeee.memo.integrate.design.pattern.behavior;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/7/21 14:28
 */
@Slf4j
public class ObserverPatternTests {

    @Test
    public void test() {
        // 具体的业务目标
        ConcreteSubject concreteSubject = new ConcreteSubject();
        // 设置监听的观察者
        concreteSubject.addObserver(receiveMsg -> log.info("我是观察者1，收到通知 receiveMsg = {}", receiveMsg));
        concreteSubject.addObserver(receiveMsg -> log.info("我是观察者2，收到通知 receiveMsg = {}", receiveMsg));
        concreteSubject.addObserver(receiveMsg -> log.info("我是观察者3，收到通知 receiveMsg = {}", receiveMsg));
        // 执行具体业务
        concreteSubject.doSomething();
    }

}
