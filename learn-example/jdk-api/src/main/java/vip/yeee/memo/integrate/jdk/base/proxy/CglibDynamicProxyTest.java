package vip.yeee.memo.integrate.jdk.base.proxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/10/25 11:32
 */
@Slf4j
public class CglibDynamicProxyTest {
    public static void main(String[] args) {
        ServiceImpl2 target = new ServiceImpl2();
        Enhancer enhancer = new Enhancer();
        // 设置代理类的父类
        enhancer.setSuperclass(target.getClass());
        // 设置回调函数
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                log.info("--------------------前置增强--------------------");
                Object result = methodProxy.invokeSuper(o, args);
                log.info("--------------------后置增强--------------------");
                return result;
            }
        });
        // 创建子类（代理对象）
        ServiceImpl2 proxyBean = (ServiceImpl2)enhancer.create();
        proxyBean.handle();
    }
}

@Slf4j
class ServiceImpl2 {
    void handle() {
        log.info("业务方法handle处理中。。。");
    }
}
