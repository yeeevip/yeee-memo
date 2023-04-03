package vip.yeee.memo.integrate.common.wxsdk.mp.config;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.error.WxRuntimeException;
import me.chanjar.weixin.common.redis.JedisWxRedisOps;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import me.chanjar.weixin.mp.util.WxMpConfigStorageHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import vip.yeee.memo.integrate.common.wxsdk.mp.service.IWxMpConfigService;
import vip.yeee.memo.integrate.common.wxsdk.mp.properties.WxMpProperties;

import javax.annotation.Resource;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * description ...
 * @author yeeeeee
 * @since 2022/2/21 18:08
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(WxMpProperties.class)
public class WxMpConfig {

    @Autowired
    private Map<String, WxMpMessageHandler> wxMpMessageHandler;
    @Resource
    private WxMpProperties wxMpProperties;
    @Resource
    private IWxMpConfigService iWxMpConfigService;

    @Bean
    public WxMpService wxMpService() {
        WxMpService service = new WxMpServiceImpl() {
            @Override
            public WxMpService switchoverTo(String mpId) {
                WxMpConfigStorageHolder.set(mpId);
                WxMpProperties.MpConfig mpConfig;
                try {
                    if (this.getWxMpConfigStorage() != null) {
                        return this;
                    }
                } catch (Exception ignore) {

                }
                if ((mpConfig = iWxMpConfigService.findMpConfigByAppId(mpId)) != null) {
                    WxMpDefaultConfigImpl configStorage;
                    if (wxMpProperties.isUseRedis()) {
                        WxMpProperties.RedisConfig redisConfig = wxMpProperties.getRedisConfig();
                        JedisPoolConfig poolConfig = new JedisPoolConfig();
                        JedisPool jedisPool = new JedisPool(poolConfig, redisConfig.getHost(), redisConfig.getPort(),
                                redisConfig.getTimeout(), redisConfig.getPassword());
                        configStorage = new WxMpRedisConfigImpl(new JedisWxRedisOps(jedisPool), mpConfig.getAppId());
                    } else {
                        configStorage = new WxMpDefaultConfigImpl();
                    }
                    configStorage.setAppId(mpConfig.getAppId());
                    configStorage.setSecret(mpConfig.getSecret());
                    configStorage.setToken(mpConfig.getToken());
                    this.addConfigStorage(mpId, configStorage);
                    return this;
                } else {
                    throw new WxRuntimeException(String.format("无法找到对应【%s】的公众号配置信息，请核实！", mpId));
                }
            }

            @Override
            public boolean switchover(String mpId) {
                try {
                    this.switchoverTo(mpId);
                    return true;
                } catch (Exception e) {
                    log.error("无法找到对应【{}】的公众号配置信息，请核实！", mpId);
                    return false;
                }
            }};
        return service;
    }

    private static final Set<String> CAN_HANDLER_MSG_TYPE_SET = new HashSet<String>() {{
        add(WxConsts.XmlMsgType.EVENT);
        add(WxConsts.XmlMsgType.TEXT);
        add(WxConsts.XmlMsgType.IMAGE);
    }};

    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {

        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        newRouter.rule().handler(logHandler);

        CAN_HANDLER_MSG_TYPE_SET.forEach(type -> {
            String beanName = type + "WxMpMessageHandler";
            if (wxMpMessageHandler.containsKey(beanName)) {
                newRouter.rule().async(false).msgType(type).handler(wxMpMessageHandler.get(beanName)).end();
            }
        });

        return newRouter;
    }

    private static final WxMpMessageHandler logHandler = new WxMpMessageHandler() {

        @Override
        public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
            log.info("收到消息：" + wxMpXmlMessage.toString());
            return null;
        }
    };

}
