package vip.yeee.memo.integrate.common.base.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/6 20:35
 */
public class IpAddressUtil {

    public static String getAddressByIp(String ip) {
        JSONObject result, resObj = JSONObject.parseObject(HttpUtil.get("http://apis.juhe.cn/ip/ipNew?key=71419bf9df89992b1f86b46adcc4ddbe&ip=" + ip));
        if(resObj != null && "200".equals(resObj.getString("resultcode")) && (result = resObj.getJSONObject("result")) != null) {
            return result.getString("Country") + result.getString("Province") + result.getString("City") + result.getString("Isp");
        }
        return null;
    }

}
