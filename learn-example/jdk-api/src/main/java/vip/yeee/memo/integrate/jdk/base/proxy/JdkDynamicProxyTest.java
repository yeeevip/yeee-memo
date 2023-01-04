package vip.yeee.memo.integrate.jdk.base.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/10/25 10:20
 */
@Slf4j
public class JdkDynamicProxyTest {
    public static void main(String[] args) {
        IService service = new ServiceImpl();
        Class<? extends IService> clazz = service.getClass();
        // 第一个参数，被代理对象的类加载器
        ClassLoader classLoader = clazz.getClassLoader();
        // 第二个参数，被代理类实现的所有接口数组
        Class<?>[] interfaces = clazz.getInterfaces();
        // 通过Proxy.newProxyInstance方法创建一个代理对象来代理IService
        IService proxyBean = (IService) Proxy.newProxyInstance(classLoader, interfaces, new InvocationHandler() {
            // 第三个参数，InvocationHandler的实现类，这里用了匿名内部类的方式
            @Override
            // 重新InvocationHandler的invoke方法，它有三个参数可以供我们使用
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 第一个参数proxy 可以通过proxy.getClass().getName()来查看对象，是一个匿名的Proxy的代理类
                log.info("proxy.getClass().getClassName() = {}", proxy.getClass().getName());
                log.info("--------------------前置增强--------------------");
                // 第二个参数method 也就是被代理对象service对象的方法，可以通过method调用service的方法
                // 第三个参数args service方法执行时传入的实际参数
                // method.invoke的返回值就是service方法的返回值
                Object invoke = method.invoke(service, args);
                log.info("--------------------后置增强--------------------");
                return invoke;
            }
        });
        proxyBean.handle();
    }
}

interface IService {
    void handle();
}

@Slf4j
class ServiceImpl implements IService {

    @Override
    public void handle() {
        log.info("业务方法handle处理中。。。");
    }
}
