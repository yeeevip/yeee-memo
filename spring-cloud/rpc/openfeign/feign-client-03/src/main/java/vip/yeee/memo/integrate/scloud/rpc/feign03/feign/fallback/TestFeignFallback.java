package vip.yeee.memo.integrate.scloud.rpc.feign03.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.scloud.rpc.feign03.feign.TestFeignService;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/3 15:15
 */
@Slf4j
@Component
public class TestFeignFallback implements TestFeignService {
    @Override
    public String getData() {
        return "发生了熔断：系统默认返回！！！";
    }
}
