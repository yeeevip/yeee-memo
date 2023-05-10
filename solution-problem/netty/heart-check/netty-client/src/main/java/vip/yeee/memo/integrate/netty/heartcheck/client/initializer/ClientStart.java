package vip.yeee.memo.integrate.netty.heartcheck.client.initializer;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import vip.yeee.memo.integrate.netty.heartcheck.client.heart.NettyClient;

/**
 * description......
 * @author yeeee
 */
public class ClientStart implements ApplicationContextInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        start();
    }

    private void start() {
        try {
            NettyClient client = new NettyClient();
            client.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
