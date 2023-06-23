package vip.yeee.memo.demo.spring.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/27 17:47
 */
@Component
@Slf4j
public class MyBeanFactoryPostProcessor1 implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        log.info("\n【PostProcessor ----> BeanFactoryPostProcessor】\n BeanDefinitionNames = {}", String.join("\n", beanDefinitionRegistry.getBeanDefinitionNames()));
        BeanDefinition testBean = beanDefinitionRegistry.getBeanDefinition("testBean");
        log.info("testBean = {}", testBean);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
