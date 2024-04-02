package vip.yeee.memo.demo.springboot.extpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 扩展点11：FactoryBean
 *
 * 在创建一些比较复杂的bean的时候，常规的方式不能使用，就可以考虑使用FactoryBean，
 * 特别其他框架技术与Spring集成的时候，如mybatis与Spring的集成(SqlSessionFactoryBean)
 *
 * 1. 执行时机
 * applicationContext.getBean -> AbstractBeanFactory.doGetBean -> getObjectForBeanInstance
 * -> FactoryBeanRegistrySupport.getObjectFromFactoryBean -> doGetObjectFromFactoryBean -> factory.getObject();
 *
 * 2. FactoryBean与BeanFactory有什么区别?
 *
 * 2.1 FactoryBean是一个工厂Bean，在需要创建比较复杂的bean的时候可以用到
 *
 * 2.2 BeanFactory是Spring bean容器的根接口，
 * 也就是说实现BeanFactory，可以得到一个最基础的Spring容器，Spring中的所有高级容器都继承了这个根接口
 *
 * 2. 应用
 * 对Bean进行属性配置，或者需要增强某些功能，采用普通的方式就比较麻烦了，
 * 这个时候我们可以声明当前类为FactoryBean的泛型，对这个Bean对象进行属性设置功能增强，再在getObject方法中获取这个Bean注入到IOC中。
 *
 * @author https://www.yeee.vip
 * @since 2023/8/14 10:01
 */
@Component
@Slf4j
public class Ext11FactoryBean implements FactoryBean<MyFactoryBean>, ApplicationContextAware, ApplicationListener<SpringApplicationEvent> {

    private ApplicationContext applicationContext;

    @Override
    public MyFactoryBean getObject() throws Exception {
        log.info("【Ext11】--------getObject-------start------------------");
        return new MyFactoryBean();
    }

    @Override
    public Class<MyFactoryBean> getObjectType() {
        log.info("【Ext11】--------getObjectType-------start------------------");
        return MyFactoryBean.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(SpringApplicationEvent event) {
        Ext11FactoryBean factoryBean = (Ext11FactoryBean)applicationContext.getBean("&ext11FactoryBean");
        MyFactoryBean myFactoryBean = (MyFactoryBean)applicationContext.getBean("ext11FactoryBean");
        try {
            log.info("【Ext11】--------factoryBean.equals(myFactoryBean) = {}-----------", factoryBean.equals(myFactoryBean));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
@Slf4j
class MyFactoryBean {

    public void doSomething() {
        log.info("【Ext11】--------MyFactoryBean.doSomething-------start------------------");
    }
}
