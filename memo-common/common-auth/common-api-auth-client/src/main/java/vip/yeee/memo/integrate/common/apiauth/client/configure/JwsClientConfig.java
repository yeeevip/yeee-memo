package vip.yeee.memo.integrate.common.apiauth.client.configure;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/30 11:21
 */
@Configuration
public class JwsClientConfig {

    @LoadBalanced
    @Bean("lbRestTemplate")
    public RestTemplate lbRestTemplate() {
        return new RestTemplate();
    }

}
