package vip.yeee.memo.demo.design.pattern.behavior;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 观察者模式，又叫发布-订阅模式（Publish/Subscribe）
 * 应用：一个对象的行为改变可能会导致其他一个或者多个对象的行为发生改变
 *
 * @author https://www.yeee.vip
 * @since 2022/7/21 11:34
 */
public class ObserverPattern {
}


/**
 * 具体目标
 */
@Slf4j
class ConcreteSubject extends Subject {

    public void doSomething() {
        // ...
        // ...
        log.info("执行业务，具体目标发生改变");
        String result = "业务方法执行完成啦！！！";
        // 通知观察者
        super.notifyObserver(result);
    }

}

/**
 * 抽象目标
 */
class Subject {

    private final CopyOnWriteArrayList<Observer> observerList = new CopyOnWriteArrayList<>();

    public void addObserver(Observer observer) {
        this.observerList.add(observer);
    }

    public void removeObserver(Observer observer) {
        this.observerList.remove(observer);
    }

    public void notifyObserver(Object receiveMsg) {
        this.observerList.parallelStream().forEach(observer -> observer.response(receiveMsg));
    };

}

/**
 * 观察者
 */
interface Observer {
    void response(Object receiveMsg);
}
