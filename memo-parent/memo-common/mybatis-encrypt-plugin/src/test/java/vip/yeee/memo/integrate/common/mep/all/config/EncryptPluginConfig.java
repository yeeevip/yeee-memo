package vip.yeee.memo.integrate.common.mep.all.config;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.yeee.memo.integrate.common.mep.Encrypt;
import vip.yeee.memo.integrate.common.mep.encrypt.AesSupport;
import vip.yeee.memo.integrate.common.mep.interceptor.DecryptReadInterceptor;
import vip.yeee.memo.integrate.common.mep.interceptor.SensitiveAndEncryptWriteInterceptor;

/**
 * 插件配置
 */
@Configuration
public class EncryptPluginConfig {


    @Bean
    Encrypt encryptor() throws Exception{ ;
       return new AesSupport("1870577f29b17d6787782f35998c4a79");
    }

    @Bean
    ConfigurationCustomizer configurationCustomizer() throws Exception{
        DecryptReadInterceptor decryptReadInterceptor = new DecryptReadInterceptor(encryptor());
        SensitiveAndEncryptWriteInterceptor sensitiveAndEncryptWriteInterceptor = new SensitiveAndEncryptWriteInterceptor(encryptor());

        return (configuration) -> {
            configuration.addInterceptor(decryptReadInterceptor);
            configuration.addInterceptor(sensitiveAndEncryptWriteInterceptor);
        };
    }
}
