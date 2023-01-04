package vip.yeee.memo.integrate.design.pattern.create;

import lombok.extern.slf4j.Slf4j;

/**
 * 单例模式
 *
 * @author yeeee
 * @since 2022/7/21 10:50
 */
public class SingletonPattern {
}

/**
 * 饿汉式
 */
@Slf4j
class InitSingleton {

    private final static InitSingleton INSTANCE = new InitSingleton();

    static {
        log.info("InitSingleton实例化时就初始化、线程安全；INSTANCE = {}", INSTANCE);
    }

    public static InitSingleton getInstance() {
        return INSTANCE;
    }

}

/**
 * 懒汉式
 */
@Slf4j
class LazySingleton {

    private static LazySingleton INSTANCE = null;

    public static LazySingleton getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (LazySingleton.class) {
            if (INSTANCE != null) {
                return INSTANCE;
            }
            INSTANCE = new LazySingleton();
            log.info("调用该方法时才初始化");
            return INSTANCE;
        }
    }

}