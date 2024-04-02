package vip.yeee.memo.demo.springboot.extpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import vip.yeee.memo.demo.springboot.extpoint.bean.TestBean01;

/**
 * 扩展点03：BeanFactoryPostProcessor
 *
 * 1. 执行时机：Spring容器初始化后、刷新前
 *
 * 容器级的后置处理器会在Spring容器初始化后、刷新前这个时间执行一次
 * refresh() -> obtainFreshBeanFactory()（BeanDefinition生成） -> invokeBeanFactoryPostProcessors(beanFactory)
 *
 * 2. 应用
 *
 * 2.1
 *  容器级别的扩展接口
 *
 * 2.2
 *  允许在容器读取到Bean的BeanDefinition数据之后，bean未实例化前，读取BeanDefinition数据，并且可以根据需要进行修改；
 *
 * 3. 如何注册到spring容器？
 *
 * 3.1 实现BeanFactoryPostProcessor接口，标记为bean
 *
 * @author https://www.yeee.vip
 * @since 2023/8/14 10:01
 */
@Slf4j
@Component
public class Ext03BeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.info("【Ext03】--------ProcessBeanFactory-------start------------------");
        TestBean01 testBean01 = beanFactory.getBean(TestBean01.class);
        log.info("【Ext03】--------ProcessBeanFactory-------testBean01 = {}------------------", testBean01);
        log.info("【Ext03】--------ProcessBeanFactory-------end------------------");
    }
}
