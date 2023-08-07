package vip.yeee.memo.common.websocket.netty.annotation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.yeee.memo.common.websocket.netty.bootstrap.ServerEndpointBootstrap;

@ConditionalOnMissingBean(ServerEndpointBootstrap.class)
@Configuration
public class NettyWebSocketSelector {

    @Bean
    public ServerEndpointBootstrap serverEndpointExporter() {
        return new ServerEndpointBootstrap();
    }
}
