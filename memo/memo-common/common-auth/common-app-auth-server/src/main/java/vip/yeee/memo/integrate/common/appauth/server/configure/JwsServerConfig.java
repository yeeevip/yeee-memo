package vip.yeee.memo.integrate.common.appauth.server.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.yeee.memo.integrate.common.appauth.server.kit.JwsServerKit;

import javax.annotation.Resource;
import java.security.KeyPair;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/11/30 11:21
 */
@Configuration
public class JwsServerConfig {

    @Resource
    private JwsServerKit jwsServerKit;

    @Bean("jwsKeyPair")
    public KeyPair keyPair() {
        return jwsServerKit.getDefaultRSAKeyPair();
    }

}
