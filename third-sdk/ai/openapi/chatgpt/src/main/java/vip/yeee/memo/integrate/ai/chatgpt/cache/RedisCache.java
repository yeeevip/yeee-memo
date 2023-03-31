package vip.yeee.memo.integrate.ai.chatgpt.cache;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/4/1 17:57
 */
@Component
public class RedisCache {
    private final Map<String, AtomicInteger> uLimitCountCache = Maps.newHashMap();
    private static final Map<String, String> configCache = Maps.newHashMap();
    static {
        configCache.put("YEEE:CHATGPT:CONFIG:ULEXCLUDE", "127.0.0.1");
        configCache.put("YEEE:CHATGPT:CONFIG:APITOKEN", "");
    }

    public Integer getULimitCountCache(String uid) {
        String uLimitKey = "YEEE:CHATGPT:ULIMIT:" + uid;
        return uLimitCountCache.get(uLimitKey).get();
    }

    public void incrULimitCountCache(String uid) {
        String uLimitKey = "YEEE:CHATGPT:ULIMIT:" + uid;
        uLimitCountCache.put(uLimitKey, new AtomicInteger(Optional.ofNullable(uLimitCountCache.get(uLimitKey)).orElse(new AtomicInteger(0)).incrementAndGet()));
//        uLimitCountCache.expire(DateUtil.endOfDay(new Date()));
    }

    public String getULimitExclude() {
        String key = "YEEE:CHATGPT:CONFIG:ULEXCLUDE";
        return configCache.get(key);
    }

    public String getApiToken() {
        String key = "YEEE:CHATGPT:CONFIG:APITOKEN";
        return configCache.get(key);
    }
}
