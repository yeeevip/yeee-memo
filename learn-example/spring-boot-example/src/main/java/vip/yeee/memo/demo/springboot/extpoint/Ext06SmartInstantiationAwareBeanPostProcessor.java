package vip.yeee.memo.demo.springboot.extpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;

/**
 * 扩展点06：SmartInstantiationAwareBeanPostProcessor
 *
 * 1. 执行时机：实例化之前/属性注入之前/属性注入后/发生循环引用
 * ·继承于InstantiationAwareBeanPostProcessor
 * ·新增能力 predictBeanType、determineCandidateConstructors、getEarlyBeanReference
 *
 * 1.1 predictBeanType
 *  在InstantiationAwareBeanPostProcessor.postProcessBeforeInstantiation()之前触发
 *
 * 1.2 determineCandidateConstructors
 *
 * 1.3 getEarlyBeanReference
 *  在InstantiationAwareBeanPostProcessor.postProcessBeforeInstantiation方法触发执行之后执行
 *
 * @author https://www.yeee.vip
 * @since 2023/8/14 10:01
 */
@Slf4j
@Component
public class Ext06SmartInstantiationAwareBeanPostProcessor implements SmartInstantiationAwareBeanPostProcessor {

    // Bean实例化前预测最终返回的Class类型;
    // 在InstantiationAwareBeanPostProcessor.postProcessBeforeInstantiation()之前触发
    @Override
    public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
        if ("testBean03".equals(beanName)) {
//            log.info("【Ext06】--------predictBeanType-------start------------------");
        }
        return null;
    }

    // 决定使用哪个构造器构造Bean，如果不指定，默认为null，即bean的无参构造方法；
    @Override
    public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException {
        if ("testBean03".equals(beanName)) {
            log.info("【Ext06】--------determineCandidateConstructors-------start------------------");
        }
        return null;
    }

    // 获得提前暴露的bean引用，主要用于Spring循环依赖问题的解决，如果Spring中检测不到循环依赖，这个方法不会被调用
    // 当存在循环依赖时，会在InstantiationAwareBeanPostProcessor.postProcessBeforeInstantiation方法触发执行之后执行；
    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        log.info("【Ext06】--------getEarlyBeanReference-------start------------------");
        return bean;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return SmartInstantiationAwareBeanPostProcessor.super.postProcessBeforeInstantiation(beanClass, beanName);
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return SmartInstantiationAwareBeanPostProcessor.super.postProcessAfterInstantiation(bean, beanName);
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return SmartInstantiationAwareBeanPostProcessor.super.postProcessProperties(pvs, bean, beanName);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return SmartInstantiationAwareBeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return SmartInstantiationAwareBeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
