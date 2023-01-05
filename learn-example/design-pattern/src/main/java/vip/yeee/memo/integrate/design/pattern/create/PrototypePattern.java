package vip.yeee.memo.integrate.design.pattern.create;

/**
 * 原型模式
 *
 * @author yeeee
 * @since 2022/7/21 18:04
 */
public class PrototypePattern {
}

class ConcreteInstance implements Cloneable {

    public ConcreteInstance clone() throws CloneNotSupportedException {
        return (ConcreteInstance) super.clone();
    }
}
