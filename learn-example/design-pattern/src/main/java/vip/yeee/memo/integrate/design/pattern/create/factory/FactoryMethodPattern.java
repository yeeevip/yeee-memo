package vip.yeee.memo.integrate.design.pattern.create.factory;

import lombok.extern.slf4j.Slf4j;

/**
 * 工厂方法模式
 *
 * @author yeeee
 * @since 2022/7/21 16:28
 */
public class FactoryMethodPattern {
}

class ConcreteBase21Factory implements FactoryMethod {

    @Override
    public Base2 getBase2() {
        return new ConcreteBase21();
    }
}

class ConcreteBase22Factory implements FactoryMethod {

    @Override
    public Base2 getBase2() {
        return new ConcreteBase22();
    }
}

interface FactoryMethod {
    Base2 getBase2();
}

@Slf4j
class ConcreteBase21 implements Base2 {

    @Override
    public void operate() {
        log.info("具体操作1");
    }
}

@Slf4j
class ConcreteBase22 implements Base2 {

    @Override
    public void operate() {
        log.info("具体操作2");
    }
}

interface Base2 {
    void operate();
}