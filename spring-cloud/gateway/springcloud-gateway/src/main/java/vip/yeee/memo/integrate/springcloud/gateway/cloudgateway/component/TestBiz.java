package vip.yeee.memo.integrate.springcloud.gateway.cloudgateway.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.common.config.properties.YeeeCommonProperties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/7/14 15:55
 */
@Slf4j
@Component
public class TestBiz {

    @Resource
    private YeeeCommonProperties yeeeCommonProperties;

    @PostConstruct
    public void init() {
        log.info("【公共配置】 - config = {}", yeeeCommonProperties);
    }

}
