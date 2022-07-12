package vip.yeee.memo.integrate.thirdsdk.weixin.mp.config;

import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.redis.JedisWxRedisOps;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import vip.yeee.memo.integrate.thirdsdk.weixin.mp.handler.WxMpEventHandler;
import vip.yeee.memo.integrate.thirdsdk.weixin.mp.properties.WxMpProperties;

import java.util.List;
import java.util.stream.Collectors;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;

/**
 * description ...
 * @author yeeeeee
 * @since 2022/2/21 18:08
 */
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties(WxMpProperties.class)
public class WxMpConfig {
    private final WxMpEventHandler wxMpEventHandler;
    private final WxMpProperties properties;

    @Bean
    public WxMpService wxMpService() {
        final List<WxMpProperties.MpConfig> configs = this.properties.getConfigs();
        if (configs == null) {
            throw new RuntimeException("not find config：wx.mp");
        }
        WxMpService service = new WxMpServiceImpl();
        service.setMultiConfigStorages(configs
            .stream().map(a -> {
                WxMpDefaultConfigImpl configStorage;
                if (this.properties.isUseRedis()) {
                    final WxMpProperties.RedisConfig redisConfig = this.properties.getRedisConfig();
                    JedisPoolConfig poolConfig = new JedisPoolConfig();
                    JedisPool jedisPool = new JedisPool(poolConfig, redisConfig.getHost(), redisConfig.getPort(),
                        redisConfig.getTimeout(), redisConfig.getPassword());
                    configStorage = new WxMpRedisConfigImpl(new JedisWxRedisOps(jedisPool), a.getAppId());
                } else {
                    configStorage = new WxMpDefaultConfigImpl();
                }

                configStorage.setAppId(a.getAppId());
                configStorage.setSecret(a.getSecret());
                configStorage.setToken(a.getToken());
                return configStorage;
            }).collect(Collectors.toMap(WxMpDefaultConfigImpl::getAppId, a -> a, (o, n) -> o)));
        return service;
    }

    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        newRouter.rule().async(false).msgType(EVENT).handler(this.wxMpEventHandler).end();

        return newRouter;
    }

}
