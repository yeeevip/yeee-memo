package vip.yeee.memo.integrate.simpletool.my;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LFUCache;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: yeeeeee
 * @Date: 2021/12/6 14:37
 */
@Slf4j
public class WxMiniAppTools {

    protected static LFUCache<String, String> prevWorkDateCache = CacheUtil.newLFUCache(365, TimeUnit.DAYS.toMillis(7));
    protected static LFUCache<String, Integer> nameMobileTwoEleCache = CacheUtil.newLFUCache(10000, TimeUnit.MINUTES.toMillis(3));

    private static String appId = null;
    private static String appSecret = null;
    public static final String keyPrefix = "onlineInterview:";
    public final static String TICKET = "linkTicket";
    protected static final AES aes = SecureUtil.aes(SecureUtil.md5(SpringContextUtils.getPropertiesValue("com.yeee.secret")).substring(0, 16).getBytes());

    public static JSONObject getWxAppConfig() {
        JSONObject conf = new JSONObject();
        conf.put("appId", appId != null ? appId : (appId = SpringContextUtils.getPropertiesValue("onlineInterview.wxapp.appId")));
        conf.put("appSecret", appSecret != null ? appSecret : (appSecret = SpringContextUtils.getPropertiesValue("onlineInterview.wxapp.appSecret")));
        return conf;
        // return (JSONObject) getCommonService().applyConfigVal(ConfigGpNameEnum.ONLINEINTERVIEW_WXMNIAPPCONFIG, JSON::parseObject);
    }

    /**
     * @Author: yeee
     * @Date: 2021/6/28 15:37
     */
    public static Object authCode2Session(String code) {
        JSONObject wxAppConfig = getWxAppConfig();
        return JSON.parseObject(HttpUtil.get(StrUtil.format("https://api.weixin.qq.com/sns/jscode2session?appid={}&secret={}&js_code={}&grant_type=authorization_code", wxAppConfig.get("appId"), wxAppConfig.get("appSecret"), code)));
    }

    /**
     * @Author: yeee
     * @Date: 2021/6/28 15:37
     */
    public static String wxAppGetAccessToken() throws Exception {
        JSONObject wxAppConfig = getWxAppConfig();
        String key = keyPrefix + "onlineWxAppToken:" + wxAppConfig.get("appId");
        String accessToken = getRedisService().getValue(key);
        if(StrUtil.isNotBlank(accessToken)) {
            return accessToken;
        }
        accessToken = JSON.parseObject(HttpUtil.get(StrUtil.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={}&secret={}", wxAppConfig.get("appId"), wxAppConfig.get("appSecret")))).getString("access_token");
        if(StrUtil.isBlank(accessToken)) {
            throw new Exception("wxAppGetAccessToken - 获取accessToken失败");
        }
        getRedisService().cacheValue(key, accessToken, 7000);
        return accessToken;
    }

    /**
     * @Author: yeee
     * @Date: 2021/6/28 15:37
     */
    public static String wxAppGenerateUrlScheme(Integer linkType, Integer orderId, Integer taskId, Integer voucherId, String linkTicket) {
        String openlink = null;
        JSONObject jsonObject = null;
        try {
            String path = Integer.valueOf(1).equals(linkType) ? "pages/experienceVerify/begining/index" : "pages/behaviorSurvey/begining/index";
            String reqParam = "{\"jump_wxa\":{\"path\":\"" + path + "\",\"query\":\"linkType=" + linkType + "&orderId=" + orderId + "&taskId=" + taskId + "&voucherId=" + voucherId + "&linkTicket=" + linkTicket +"\"},\"is_expire\":true,\"expire_time\":" + DateUtil.offsetDay(new Date(), 360).getTime()/1000 + "}";
            String res = HttpUtil.post("https://api.weixin.qq.com/wxa/generatescheme?access_token=" + wxAppGetAccessToken(), reqParam);
            openlink = (jsonObject = JSON.parseObject(res)).getString("openlink");
            if(StrUtil.isBlank(openlink)) {
                throw new Exception(StrUtil.format("wxAppGenerateUrlScheme - 获取UrlScheme失败 - res = {}", res));
            }
        } catch (Exception e) {
            log.error("wxAppGenerateUrlScheme err", e);
            if(jsonObject != null && "40001,42001".contains(jsonObject.getString("errcode"))) {
                getRedisService().removeValue( keyPrefix + "onlineWxAppToken:" + getWxAppConfig().get("appId"));
            }
        }
        return openlink;
    }

    /**
     * @Author: yeee
     * @Date: 2021/6/28 15:37
     */
    public static String wxAppGenerateUrlLink(JSONObject confObj, Integer linkId, Integer linkType) {
        String openlink = null;
        try {
            String reqParam = "{\"path\":\"/pages/index\",\"query\":\"linkId=100&linkType=100\",\"is_expire\":true,\"expire_type\":1,\"expire_interval\":31}";
            openlink = JSON.parseObject(HttpUtil.post("https://api.weixin.qq.com/wxa/generate_urllink?access_token=" + wxAppGetAccessToken(), reqParam)).getString("openlink");
            if(StrUtil.isBlank(openlink)) {
                throw new Exception("wxAppGenerateUrlLink - 获取UrlLink失败");
            }
        } catch (Exception e) {
            log.error("wxAppGenerateUrlLink err", e);
        }
        return openlink;
    }

    /**
     * @Description: 匿名接口的安全措施
     * @Author: yeee一页
     * @Date: 2021/6/28 15:37
     */
    public static String requestIdentity(Integer tt, HttpServletRequest request, Object... args) throws Exception {
        if(Integer.valueOf(1).equals(tt)) {
            return aes.encryptHex(Arrays.stream(args).map(String::valueOf).sorted().collect(Collectors.joining("*")));
        }
        try {
            String linkTicket = request.getHeader(TICKET);
            if(StrUtil.isBlank(linkTicket) || !aes.decryptStr(linkTicket).equals(Arrays.stream(args).map(String::valueOf).sorted().collect(Collectors.joining("*")))) {
                throw new OnlineInterviewBizException();
            }
        } catch (Exception e) {
            throw new OnlineInterviewBizException(OnlineInterviewBizException.BizErrEnum.NOT_OPR_POWER);
        }
        return null;
    }

    private static RedisService getRedisService() {
        return new RedisService() {};
    }

    interface RedisService {
        default String getValue(String key) {
            return "test";
        }

        default boolean cacheValue(String key, String val, long ttl) {
            return true;
        }

        default boolean removeValue(String key) {
            return true;
        }
    }

}
