package vip.yeee.memo.common.appauth.server.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.yeee.memo.common.appauth.server.kit.JwsServerKit;

import javax.annotation.Resource;
import java.security.KeyPair;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/11/30 11:21
 */
@ConditionalOnBean(JwsServerKit.class)
@Configuration
public class JwsServerConfig {

    @Resource
    private JwsServerKit jwsServerKit;

    @Bean("jwsKeyPair")
    public KeyPair keyPair() {
        return jwsServerKit.getDefaultRSAKeyPair();
    }

}
