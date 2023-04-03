package vip.yeee.memo.integrate.common.wxsdk.mp.service;

import vip.yeee.memo.integrate.common.wxsdk.mp.properties.WxMpProperties;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/4/8 14:29
 */
public interface IWxMpConfigService {

    WxMpProperties.MpConfig findMpConfigByAppId(String appId);

}
