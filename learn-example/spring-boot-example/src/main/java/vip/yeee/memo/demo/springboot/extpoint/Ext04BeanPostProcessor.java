package vip.yeee.memo.demo.springboot.extpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import vip.yeee.memo.demo.springboot.extpoint.bean.TestBean03;

/**
 * 扩展点04：BeanPostProcessor
 *
 * 1. 执行时机：实例化、属性注入后在invokeInitMethods方法前后执行
 *
 * 1.1 注册BeanPostProcessor
 *  refresh() -> registerBeanPostProcessors(beanFactory);
 *
 * 1.2 执行BeanPostProcessor
 *  refresh() -> finishBeanFactoryInitialization(beanFactory) -> preInstantiateSingletons() -> getBean(beanName)
 *  -> doGetBean -> createBean -> doCreateBean（createBeanInstance（实例化）、populateBean（属性注入））
 *  -> 【initializeBean】
 *  -> （applyBeanPostProcessorsBeforeInitialization ———— invokeInitMethods  ———— applyBeanPostProcessorsAfterInitialization）
 *
 * 2. 应用
 * Bean级别的扩展接口，实例化、属性注入后执行
 *
 * 2.1
 *  属性值修改
 *
 * 2.2
 *  自定义初始化逻辑；例如，在初始化之前或之后执行一些额外的初始化逻辑，如数据加载、资源释放等。
 *
 * 3. 如何注册到spring容器？
 *
 * 3.1 实现BeanPostProcessor接口，按需重写before、after方法，标记为bean
 *
 * @author yeeee
 * @since 2023/8/14 10:01
 */
@Slf4j
@Component
public class Ext04BeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof TestBean03) {
            log.info("【Ext04】--------BeforeInitialization-------start------------------");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof TestBean03) {
            log.info("【Ext04】--------AfterInitialization-------start------------------");
        }
        return bean;
    }
}
