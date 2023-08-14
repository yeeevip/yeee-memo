package vip.yeee.memo.demo.springboot.extpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * 扩展点11：FactoryBean
 *
 * 1. 执行时机

 *
 * 2. FactoryBean与BeanFactory有什么区别?
 *
 * 2.1 FactoryBean是一个工厂Bean，在需要创建比较复杂的bean的时候可以用到
 *
 * 2.2 BeanFactory是Spring bean容器的根接口，
 * 也就是说实现BeanFactory，可以得到一个最基础的Spring容器，Spring中的所有高级容器都继承了这个根接口
 *
 * @author yeeee
 * @since 2023/8/14 10:01
 */
@Component
@Slf4j
public class Ext11FactoryBean implements FactoryBean {


    @Override
    public Object getObject() throws Exception {
        log.info("【Ext11】--------getObject-------start------------------");
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        log.info("【Ext11】--------getObjectType-------start------------------");
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
