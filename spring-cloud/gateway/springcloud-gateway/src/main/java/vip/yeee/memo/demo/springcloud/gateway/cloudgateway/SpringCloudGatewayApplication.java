package vip.yeee.memo.demo.springcloud.gateway.cloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import vip.yeee.memo.common.scloud.gray.gateway.configure.GrayGatewayLoadBalancerConfig;

//@SpringCloudApplication
//@LoadBalancerClient(name = "cloud-web-auth-server", configuration = GrayGatewayLoadBalancerConfig.class)
@LoadBalancerClients(defaultConfiguration = GrayGatewayLoadBalancerConfig.class)
@SpringBootApplication
@EnableDiscoveryClient
public class SpringCloudGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudGatewayApplication.class, args);
    }

}
