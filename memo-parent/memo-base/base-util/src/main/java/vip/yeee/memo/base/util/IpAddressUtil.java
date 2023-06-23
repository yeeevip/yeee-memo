package vip.yeee.memo.base.util;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/6 20:35
 */
public class IpAddressUtil {

    public static String getAddressByIp(String ip) {
        String address = getAddressByIp1(ip);
        if (StrUtil.isNotBlank(address)) {
            return address;
        }
        return getAddressByIp2(ip);
    }

    private static String getAddressByIp1(String ip) {
        JSONObject result, resObj = JSONObject.parseObject(HttpUtil.get("http://apis.juhe.cn/ip/ipNew?key=71419bf9df89992b1f86b46adcc4ddbe&ip=" + ip));
        if(resObj != null && "200".equals(resObj.getString("resultcode")) && (result = resObj.getJSONObject("result")) != null) {
            return result.getString("Country") + result.getString("Province") + result.getString("City") + result.getString("Isp");
        }
        return null;
    }

    private static String getAddressByIp2(String ip) {
        try {
            Future<String> future = ThreadUtil.execAsync(() -> {
                String api = String.format("http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true", ip);
                cn.hutool.json.JSONObject object = JSONUtil.parseObj(HttpUtil.get(api));
                return object.get("addr", String.class);
            });
            return future.get(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            //log.error("ip city info error", e);
        }
        return null;
    }

}
