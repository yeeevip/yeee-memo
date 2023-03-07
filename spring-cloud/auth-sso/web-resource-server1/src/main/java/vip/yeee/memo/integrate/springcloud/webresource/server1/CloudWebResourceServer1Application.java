package vip.yeee.memo.integrate.springcloud.webresource.server1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import vip.yeee.memo.integrate.common.scloud.gray.inner.configure.GrayInnerLoadBalancerConfig;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/21 11:33
 */
//@LoadBalancerClient(name = "cloud-web-resources-server1", configuration = GrayInnerLoadBalancerConfig.class)
@LoadBalancerClients(defaultConfiguration = GrayInnerLoadBalancerConfig.class)
@EnableFeignClients
@SpringBootApplication
public class CloudWebResourceServer1Application {

    public static void main(String[] args) {
        SpringApplication.run(CloudWebResourceServer1Application.class, args);
    }

}
