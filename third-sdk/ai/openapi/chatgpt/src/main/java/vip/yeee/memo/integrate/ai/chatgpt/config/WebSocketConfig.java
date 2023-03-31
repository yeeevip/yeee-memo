package vip.yeee.memo.integrate.ai.chatgpt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {
    /**
     * 这个bean的注册,用于扫描带有@ServerEndpoint的注解成为websocket,如果你使用外置的tomcat就不需要该配置文件
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
