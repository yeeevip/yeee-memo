package vip.yeee.memo.demo.design.pattern.create.factory;

import lombok.extern.slf4j.Slf4j;

/**
 * 简单工程模式
 *
 * @author https://www.yeee.vip
 * @since 2022/7/21 15:20
 */
public class SimpleFactoryPattern {

    public static Base getBase(String name) {
        if ("base1".equals(name)) {
            return new ConcreteBase1();
        } else if("base2".equals(name)) {
            return new ConcreteBase2();
        }
        return null;
    }

}

@Slf4j
class ConcreteBase1 implements Base {

    @Override
    public void operate() {
        log.info("具体处理1");
    }
}

@Slf4j
class ConcreteBase2 implements Base {

    @Override
    public void operate() {
        log.info("具体处理2");
    }
}

interface Base {
    void operate();
}
