package vip.yeee.memo.common.wxsdk.ma.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaKefuMessage;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedisConfigImpl;
import cn.binarywang.wx.miniapp.message.WxMaMessageHandler;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import cn.binarywang.wx.miniapp.message.WxMaXmlOutMessage;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.error.WxRuntimeException;
import me.chanjar.weixin.common.session.WxSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import vip.yeee.memo.common.wxsdk.ma.properties.WxMaProperties;
import vip.yeee.memo.common.wxsdk.ma.service.IWxMaConfigService;

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
@EnableConfigurationProperties(WxMaProperties.class)
@Configuration
public class WxMaConfigure {

    @Autowired
    private Map<String, WxMaMessageHandler> wxMaMessageHandler;
    @Resource
    private WxMaProperties wxMaProperties;
    @Resource
    private IWxMaConfigService iWxMaConfigService;

    @Bean
    public WxMaService wxMaService() {
        WxMaService service = new WxMaServiceImpl() {
            @Override
            public WxMaService switchoverTo(String appId) {
                WxMaConfigHolder.set(appId);
                WxMaProperties.MaConfig maConfig;
                try {
                    if (this.getWxMaConfig() != null) {
                        return this;
                    }
                } catch (Exception ignore) {

                }
                if ((maConfig = iWxMaConfigService.findMaConfigByAppId(appId)) != null) {
                    WxMaDefaultConfigImpl configStorage;
                    if (wxMaProperties.isUseRedis()) {
                        WxMaProperties.RedisConfig redisConfig = wxMaProperties.getRedisConfig();
                        JedisPoolConfig poolConfig = new JedisPoolConfig();
                        JedisPool jedisPool = new JedisPool(poolConfig, redisConfig.getHost(), redisConfig.getPort(),
                                redisConfig.getTimeout(), redisConfig.getPassword());
                        configStorage = new WxMaRedisConfigImpl(jedisPool);
                    } else {
                        configStorage = new WxMaDefaultConfigImpl();
                    }
                    configStorage.setAppid(maConfig.getAppId());
                    configStorage.setSecret(maConfig.getSecret());
                    configStorage.setToken(maConfig.getToken());
                    this.addConfig(appId, configStorage);
                    return this;
                } else {
                    throw new WxRuntimeException(String.format("无法找到对应【%s】的小程序配置信息，请核实！", appId));                }
            }

            @Override
            public boolean switchover(String appId) {
                try {
                    this.switchoverTo(appId);
                    return true;
                } catch (Exception e) {
                    log.error("无法找到对应【{}】的小程序配置信息，请核实！", appId);
                    return false;
                }
            }};
        return service;
    }

    private static final Set<String> CAN_HANDLER_MSG_TYPE_SET = new HashSet<String>() {{
        add(WxConsts.XmlMsgType.TEXT);
        add(WxConsts.XmlMsgType.IMAGE);
    }};

    @Bean
    public WxMaMessageRouter wxMaMessageRouter(WxMaService wxMaService) {

        final WxMaMessageRouter newRouter = new WxMaMessageRouter(wxMaService);
//
//        newRouter.rule().handler(logHandler).next()
//                .rule().async(false).content("文本").handler(textHandler).end()
//                .rule().async(false).content("图片").handler(picHandler).end()
//                .rule().async(false).content("二维码").handler(qrcodeHandler).end()
//                .rule().async(false).content("转发客服").handler(customerServiceMessageHandler).end();

        newRouter.rule().handler(logHandler);

        CAN_HANDLER_MSG_TYPE_SET.forEach(type -> {
            String beanName = type + "WxMaMessageHandler";
            if (wxMaMessageHandler.containsKey(beanName)) {
                newRouter.rule().async(false).msgType(type).handler(wxMaMessageHandler.get(beanName)).end();
            }
        });

        return newRouter;
    }

    private static final WxMaMessageHandler logHandler = new WxMaMessageHandler() {
        @Override
        public WxMaXmlOutMessage handle(WxMaMessage wxMessage, Map<String, Object> context,
                                        WxMaService service, WxSessionManager sessionManager) throws WxErrorException {
            System.out.println("收到消息：" + wxMessage.toString());
            service.getMsgService().sendKefuMsg(WxMaKefuMessage.newTextBuilder().content("收到信息为：" + wxMessage.toJson())
                    .toUser(wxMessage.getFromUser()).build());
            return null;
        }
    };

}
