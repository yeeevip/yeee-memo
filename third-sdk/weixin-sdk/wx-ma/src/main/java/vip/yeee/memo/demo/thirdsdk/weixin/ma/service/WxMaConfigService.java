package vip.yeee.memo.demo.thirdsdk.weixin.ma.service;

import org.springframework.stereotype.Service;
import vip.yeee.memo.base.web.utils.SpringContextUtils;
import vip.yeee.memo.common.wxsdk.ma.properties.WxMaProperties;
import vip.yeee.memo.common.wxsdk.ma.service.IWxMaConfigService;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/4/8 14:43
 */
@Service
public class WxMaConfigService implements IWxMaConfigService {

    @Override
    public WxMaProperties.MaConfig findMaConfigByAppId(String appId) {
        WxMaProperties configPo = (WxMaProperties)SpringContextUtils.getBean(WxMaProperties.class);
        for (WxMaProperties.MaConfig config : configPo.getConfigs()) {
            if (appId.equals(config.getAppId())) {
                return config;
            }
        }
        return null;
    }
}
