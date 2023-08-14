package vip.yeee.memo.demo.springboot.extpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.stereotype.Component;

/**
 * 扩展点10：SmartInitializingSingleton
 *
 * 1. 执行时机
 *  单例bean已经完成实现化、属性注入以及相关的初始化操作
 *  refresh() -> finishBeanFactoryInitialization(beanFactory) -> preInstantiateSingletons()
 *  -> bean初始化之后
 *  -> 遍历所有实现了SmartInitializingSingleton的bean，smartSingleton.afterSingletonsInstantiated();
 *
 * 2.
 *
 * 2.1 实现SmartInitializingSingleton接口的bean的作用域必须是单例，afterSingletonsInstantiated()才会触发；
 *
 * @author yeeee
 * @since 2023/8/14 10:01
 */
@Component
@Slf4j
public class Ext10SmartInitializingSingleton implements SmartInitializingSingleton {

    @Override
    public void afterSingletonsInstantiated() {
        log.info("【Ext10】--------afterSingletonsInstantiated-------start------------------");
    }
}
