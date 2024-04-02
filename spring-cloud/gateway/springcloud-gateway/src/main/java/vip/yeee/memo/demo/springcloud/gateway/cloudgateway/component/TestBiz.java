package vip.yeee.memo.demo.springcloud.gateway.cloudgateway.component;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import vip.yeee.memo.base.config.properties.YeeeCommonProperties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/7/14 15:55
 */
@Slf4j
@RefreshScope // Nacos @ConfigurationProperties可以直接刷新，@Value需要加@RefreshScope
@Component
public class TestBiz {

    @Resource
    private YeeeCommonProperties yeeeCommonProperties;
    @Value("${yeee.common.name}")
    private String name;

    @PostConstruct
    public void init() {
        log.info("【公共配置】 - config = {}", yeeeCommonProperties);
    }

    public Object testConfigPropertyChange() {
        return ImmutableMap.of("c1", yeeeCommonProperties, "c2", name);
    }
}
