package vip.yeee.memo.integrate.learn.spring.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/27 17:44
 */
@Component
@Slf4j
public class MyBeanPostProcessor1 implements InstantiationAwareBeanPostProcessor {

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        log.info("\n【PostProcessor ----> BeanPostProcessor】\n BeforeInitialization，beanName = {}", beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("\n【PostProcessor ----> BeanPostProcessor】\n AfterInitialization，beanName = {}", beanName);
        return bean;
    }
}
