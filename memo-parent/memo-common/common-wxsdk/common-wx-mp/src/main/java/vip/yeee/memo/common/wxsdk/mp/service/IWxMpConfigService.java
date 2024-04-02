package vip.yeee.memo.common.wxsdk.mp.service;

import vip.yeee.memo.common.wxsdk.mp.properties.WxMpProperties;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/4/8 14:29
 */
public interface IWxMpConfigService {

    WxMpProperties.MpConfig findMpConfigByAppId(String appId);

}
