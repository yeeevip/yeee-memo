package vip.yeee.memo.demo.springboot.extpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 扩展点13：DisposableBean
 * 是在Spring容器关闭的时候预留的一个扩展点，在Spring容器销毁bean的时候获得一次回调
 *
 * 1. 执行时机：Spring容器关闭，需要销毁所有的bean时
 * AbstractApplicationContext.registerShutdownHook -> doClose() -> DefaultSingletonBeanRegistry. destroySingletons
 * -> destroyBean -> bean.destroy();
 *
 * 2. 应用
 * 如果某些Bean在容器关闭后，需要做一些【释放业务资源】之类的操作，就能用到这个扩展点了
 *
 *
 * @author https://www.yeee.vip
 * @since 2023/8/14 10:01
 */
@Slf4j
@Component
public class Ext13DisposableBean implements DisposableBean, ApplicationListener<SpringApplicationEvent> {

    @Override
    public void destroy() throws Exception {
        log.info("【Ext13】--------destroy-------start------------------");
    }

    @Override
    public void onApplicationEvent(SpringApplicationEvent event) {
//        throw new RuntimeException();
    }
}
