package vip.yeee.memo.common.wxsdk.ma.service;

import vip.yeee.memo.common.wxsdk.ma.properties.WxMaProperties;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/4/8 14:29
 */
public interface IWxMaConfigService {

    WxMaProperties.MaConfig findMaConfigByAppId(String appId);

}
