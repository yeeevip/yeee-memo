package vip.yeee.memo.demo.springboot.extpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 扩展点05：InstantiationAwareBeanPostProcessor
 *
 * 1. 执行时机：实例化之前/属性注入之前/属性注入后
 *
 * 1.1 继承于BeanPostProcessor
 *  postProcessBeforeInitialization/postProcessAfterInitialization属性注入后执行
 *
 * 1.2 postProcessBeforeInstantiation
 *  实例化之前
 *
 * 1.3 postProcessAfterInstantiation/postProcessProperties
 *  实例化之后、属性注入之前
 *
 * 2. 应用
 * 继承于BeanPostProcessor，提供了更细粒度的控制和定制Bean实例化过程的能力，可在bean实例化前后执行相应的逻辑
 *
 * @author https://www.yeee.vip
 * @since 2023/8/14 10:01
 */
@Slf4j
@Component
public class Ext05InstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if ("testBean03".equals(beanName)) {
            log.info("【Ext05】--------postProcessBeforeInstantiation-------start------------------");
        }
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if ("testBean03".equals(beanName)) {
            log.info("【Ext05】--------postProcessAfterInstantiation-------start------------------");
        }
        return true;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if ("testBean03".equals(beanName)) {
            log.info("【Ext05】--------postProcessProperties-------start------------------");
        }
        return null;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return InstantiationAwareBeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return InstantiationAwareBeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
