package com.learn.jetcache.cache;

import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/6/22 18:27
 */
@Slf4j
@Component
public class CacheBizService {

    @CacheRefresh(refresh = 30, stopRefreshAfterLastAccess = 3600)
    @CachePenetrationProtect
    @Cached(cacheType = CacheType.BOTH, expire = 30, localExpire = 16, localLimit = 20)
    public Map<String, Object> getData() {
        log.info("-----------------查询数据-----------------");
        HashMap<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        map.put("key4", "value4");
        map.put("key5", "value5");
        map.put("key6", "value6");
        return map;
    }

}
