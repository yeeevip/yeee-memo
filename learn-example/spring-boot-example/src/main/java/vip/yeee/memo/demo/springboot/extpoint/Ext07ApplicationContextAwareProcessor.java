package vip.yeee.memo.demo.springboot.extpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

/**
 * 扩展点07：ApplicationContextAwareProcessor
 *
 * ApplicationContextAwareProcessor本身并不是扩展点，而是BeanPostProcessor扩展接口的具体实现（非public类）
 * 其内部有6个扩展点可供实现，分别是EnvironmentAware、EmbeddedValueResolverAware、ResourceLoaderAware、ApplicationEventPublisherAware、MessageSourceAware、ApplicationContextAware
 *
 * 1. 执行时机
 *
 * 1.1 注册时机
 *  refresh() -> prepareBeanFactory(beanFactory) -> beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
 *
 * 1.2 执行逻辑
 *  继承BeanPostProcessor重写postProcess[Before]Initialization
 *  判断当前Bean是否实现了XxxAware，如果不是，则直拉返回，如果是，则执行XxxAware接口的扩展逻辑
 *
 * 1.2 执行时机
 *  继承BeanPostProcessor，与前面BeanPostProcessor的执行时机一致
 *
 *
 * @author https://www.yeee.vip
 * @since 2023/8/14 10:01
 */
@Slf4j
@Component
public class Ext07ApplicationContextAwareProcessor implements EnvironmentAware, EmbeddedValueResolverAware
        , ResourceLoaderAware, ApplicationEventPublisherAware, MessageSourceAware, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private ApplicationEventPublisher applicationEventPublisher;

    // 用于获取Environment，Environment可以获得系统内的所有参数；另外也可以通过注入的方式来获得Environment
    @Override
    public void setEnvironment(Environment environment) {
        log.info("【Ext07】--------setEnvironment-------start------------------");
    }

    // 用于获取StringValueResolver，StringValueResolver可以获取基于String类型的properties的变量；
    // 另外还可以使用@Value的方式来获取properties的变量
    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        log.info("【Ext07】--------setEmbeddedValueResolver-------start------------------");
    }

    // 用于获取ResourceLoader，ResourceLoader可以用于获取classpath内所有的资源对象。
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        log.info("【Ext07】--------setResourceLoader-------start------------------");
    }

    // 用于获取ApplicationEventPublisher，ApplicationEventPublisher可以用来发布事件，
    // 当然这个对象也可以通过spring注入的方式来获得
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        log.info("【Ext07】--------setApplicationEventPublisher-------start------------------");
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // 用于获取MessageSource，MessageSource主要用来做国际化
    @Override
    public void setMessageSource(MessageSource messageSource) {
        log.info("【Ext07】--------setMessageSource-------start------------------");
    }

    // 用来获取ApplicationContext，ApplicationContext就是Spring上下文管理器
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("【Ext07】--------setApplicationContext-------start------------------");
        this.applicationContext = applicationContext;
    }
}
