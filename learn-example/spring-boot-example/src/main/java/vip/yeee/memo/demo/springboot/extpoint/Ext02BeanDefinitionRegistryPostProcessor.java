package vip.yeee.memo.demo.springboot.extpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;
import vip.yeee.memo.demo.springboot.extpoint.bean.TestBean02;

/**
 * 扩展点02：BeanDefinitionRegistryPostProcessor
 *
 * 1. 执行时机
 *
 * 容器级的后置处理器会在Spring容器初始化后、刷新前这个时间执行一次
 * refresh() -> obtainFreshBeanFactory()（BeanDefinition生成） -> invokeBeanFactoryPostProcessors(beanFactory)
 *
 * 2. 应用
 *
 * 2.1
 *  容器级别的扩展接口，继承了BeanFactoryPostProcessor
 *
 * 2.2
 *  postProcessBeanDefinitionRegistry()方法可以通过BeanDefinitionRegistry对BeanDefinition进行增删改查
 *
 * 3. 如何注册到spring容器？
 *
 * 3.1 实现BeanDefinitionRegistryPostProcessor接口，标记为bean
 *
 * @author yeeee
 * @since 2023/8/14 10:01
 */
@Slf4j
@Component
public class Ext02BeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        log.info("【Ext02】--------ProcessBeanDefinitionRegistry-------start------------------");
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(TestBean02.class);
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        PropertyValue propertyValue1 = new PropertyValue("field1", "属性1");
        PropertyValue propertyValue2 = new PropertyValue("field2", "属性2");
        propertyValues.addPropertyValue(propertyValue1);
        propertyValues.addPropertyValue(propertyValue2);
        beanDefinition.setPropertyValues(propertyValues);
        //注册手工定义的beanDefinition
        registry.registerBeanDefinition("testBean02", beanDefinition);
        log.info("【Ext02】--------ProcessBeanDefinitionRegistry-------end------------------");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.info("【Ext02】--------ProcessBeanFactory-------start------------------");
        TestBean02 testBean02 = beanFactory.getBean(TestBean02.class);
        log.info("【Ext02】--------ProcessBeanFactory-------testBean02 = {}------------------", testBean02);
        log.info("【Ext02】--------ProcessBeanFactory-------end------------------");
    }
}
