package vip.yeee.memo.demo.design.pattern.create.factory;

import org.junit.jupiter.api.Test;

/**
 * 简单工厂模式
 *
 * @author yeeee
 * @since 2022/7/21 16:10
 */
public class FactoryPatternTests {

    @Test
    public void testSimpleFactoryPattern() {
        Base base1 = SimpleFactoryPattern.getBase("base1");
        base1.operate();
        Base base2 = SimpleFactoryPattern.getBase("base2");
        base2.operate();
    }

    @Test
    public void testFactoryMethodPattern() {
        FactoryMethod base1Factory = new ConcreteBase21Factory();
        base1Factory.getBase2().operate();
        FactoryMethod base2Factory = new ConcreteBase22Factory();
        base2Factory.getBase2().operate();
    }

    @Test
    public void testAbstractFactoryPattern() {
        AnimalAbstractFactory dogFactory = new DogFactory();
        dogFactory.getAnimal().name();
        dogFactory.getAnimalDesc().desc();
        AnimalAbstractFactory catFactory = new CatFactory();
        catFactory.getAnimal().name();
        catFactory.getAnimalDesc().desc();
    }

}
