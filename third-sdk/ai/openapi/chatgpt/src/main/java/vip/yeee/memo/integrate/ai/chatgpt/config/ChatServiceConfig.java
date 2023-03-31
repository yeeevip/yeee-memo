package vip.yeee.memo.integrate.ai.chatgpt.config;

import vip.yeee.memo.integrate.ai.chatgpt.service.ChatAuthService;
import vip.yeee.memo.integrate.ai.chatgpt.service.impl.DefaultChatAuthServiceImpl;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * chat服务配置信息
 */
@SpringBootConfiguration
public class ChatServiceConfig {
    // Chat 鉴权服务
    @Bean
    @ConditionalOnMissingBean(value = ChatAuthService.class)
    public ChatAuthService chatAuthService(){
        return new DefaultChatAuthServiceImpl();
    }
}
