package vip.yeee.memo.demo.springboot.extpoint;

import lombok.extern.slf4j.Slf4j;

/**
 * 扩展点09：InitializingBean
 * InitializingBean接口，帮助用户实现一些自定义的初始化操作
 *
 *
 * 1. 执行时机
 *  Bean实例化、属性注入后执行重写的afterPropertiesSet()
 *
 * 1.1 与init-method效果类似，但afterPropertiesSet()早于init-method
 *
 * 1.2 直接调用bean的afterPropertiesSet()方法
 *  refresh() -> finishBeanFactoryInitialization(beanFactory) -> preInstantiateSingletons() -> getBean(beanName)
 *  -> doGetBean -> createBean -> doCreateBean（createBeanInstance（实例化）、populateBean（属性注入））
 *  -> initializeBean -> ((InitializingBean) bean).afterPropertiesSet()
 *
 * 2. 应用
 * 可以用于修改默认设置的属性、添加补充额外的属性值，或者针对关键属性做校验
 *
 * @author yeeee
 * @since 2023/8/14 10:01
 */
@Slf4j
public class Ext09InitializingBean {

}
