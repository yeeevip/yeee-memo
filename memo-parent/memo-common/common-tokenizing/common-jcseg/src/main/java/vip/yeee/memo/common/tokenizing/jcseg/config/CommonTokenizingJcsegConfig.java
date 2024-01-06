package vip.yeee.memo.common.tokenizing.jcseg.config;

import cn.hutool.extra.tokenizer.TokenizerEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.yeee.memo.common.tokenizing.jcseg.customize.ADictionaryExtra;

/**
 * description......
 *
 * @author yeeee
 * @since 2024/1/6 22:46
 */
@Configuration
public class CommonTokenizingJcsegConfig {

    @Bean
    public TokenizerEngine tokenizerEngine() {
        return ADictionaryExtra.getTokenizerEngine();
    }
}
