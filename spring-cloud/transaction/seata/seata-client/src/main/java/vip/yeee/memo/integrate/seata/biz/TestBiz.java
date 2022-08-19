package vip.yeee.memo.integrate.seata.biz;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.seata.feign.TestService1FeignClient;
import vip.yeee.memo.integrate.seata.feign.TestService2FeignClient;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/18 18:29
 */
@Component
public class TestBiz {

    @Resource
    private TestService1FeignClient testService1FeignClient;
    @Resource
    private TestService2FeignClient testService2FeignClient;

    @GlobalTransactional(rollbackFor = Exception.class)
    public void testSeataService() {
        testService1FeignClient.testSeataService1();
        testService2FeignClient.testSeataService2();
    }

}
