package vip.yeee.memo.demo.netty.heartcheck.client.initializer;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import vip.yeee.memo.demo.netty.heartcheck.client.heart.NettyClient;

/**
 * description......
 * @author https://www.yeee.vip
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
