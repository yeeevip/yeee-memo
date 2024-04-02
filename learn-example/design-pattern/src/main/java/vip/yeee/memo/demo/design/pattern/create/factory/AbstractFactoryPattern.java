package vip.yeee.memo.demo.design.pattern.create.factory;

import lombok.extern.slf4j.Slf4j;

/**
 * 抽象工厂
 *
 * @author https://www.yeee.vip
 * @since 2022/7/21 16:46
 */
public class AbstractFactoryPattern {
}

class CatFactory implements AnimalAbstractFactory {

    @Override
    public Animal getAnimal() {
        return new Cat();
    }

    @Override
    public AnimalDesc getAnimalDesc() {
        return new CatDesc();
    }
}

class DogFactory implements AnimalAbstractFactory {

    @Override
    public Animal getAnimal() {
        return new Dog();
    }

    @Override
    public AnimalDesc getAnimalDesc() {
        return new DogDesc();
    }
}

interface AnimalAbstractFactory {
    Animal getAnimal();
    AnimalDesc getAnimalDesc();
}

@Slf4j
class DogDesc implements AnimalDesc {

    @Override
    public void desc() {
        log.info("狗的特点是。。。");
    }
}

@Slf4j
class CatDesc implements AnimalDesc {

    @Override
    public void desc() {
        log.info("猫的特点是。。。");
    }
}

interface AnimalDesc {
    void desc();
}

@Slf4j
class Dog implements Animal {

    @Override
    public void name() {
        log.info("我是狗");
    }
}

@Slf4j
class Cat implements Animal {

    @Override
    public void name() {
        log.info("我是猫");
    }
}

interface Animal {
    void name();
}
