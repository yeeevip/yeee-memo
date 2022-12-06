package vip.yeee.integrate.springcloud.webauth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import vip.yeee.memo.integrate.common.scloud.gray.inner.configure.GrayInnerLoadBalancerConfig;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/16 17:29
 */
//@LoadBalancerClient(name = "cloud-web-auth-resources1", configuration = GrayInnerLoadBalancerConfig.class)
@LoadBalancerClients(defaultConfiguration = GrayInnerLoadBalancerConfig.class)
@EnableFeignClients
@SpringBootApplication
public class CloudWebauthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudWebauthServerApplication.class, args);
    }

}
