package vip.yeee.memo.demo.springcloud.webresource.server1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import vip.yeee.memo.common.scloud.gray.inner.configure.GrayInnerLoadBalancerConfig;

import java.util.Map;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/11/21 11:33
 */
//@LoadBalancerClient(name = "cloud-web-resources-server1", configuration = GrayInnerLoadBalancerConfig.class)
@LoadBalancerClients(defaultConfiguration = GrayInnerLoadBalancerConfig.class)
@EnableFeignClients
@SpringBootApplication
public class CloudWebResourceServer1Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CloudWebResourceServer1Application.class, args);
//        Map<String, RedisTemplate> templateMap = context.getBeansOfType(RedisTemplate.class);
//        templateMap.forEach((k, v) -> {
//            System.out.println(k + " setValue！");
//            v.opsForValue().set("rdsKey", k);
//        });
//        templateMap.forEach((k, v) -> {
//            System.out.println(k + " getValue！");
//            System.out.println(v.opsForValue().get("rdsKey"));
//        });
    }

}
