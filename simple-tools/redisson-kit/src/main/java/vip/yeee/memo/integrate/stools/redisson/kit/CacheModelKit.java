package vip.yeee.memo.integrate.stools.redisson.kit;

import cn.hutool.core.collection.CollectionUtil;
import org.redisson.api.*;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/19 15:08
 */
@Component
public class CacheModelKit {

    @Resource
    private RedissonClient redissonClient;
    private final Codec codec = new JsonJacksonCodec();

    public void cacheObject(String key, Object val, long liveTime) {
        redissonClient.getBucket(key, codec).set(val, liveTime, TimeUnit.SECONDS);
    }

    public Object getObject(String key) {
        return redissonClient.getBucket(key, codec).get();
    }

    public void cacheList(String key, List<?> eleList, long liveTime) {
        RList<Object> rList = redissonClient.getList(key, codec);
        rList.addAll(eleList);
        rList.expire(liveTime, TimeUnit.SECONDS);
    }

    public RList<?> getList(String key) {
        return redissonClient.getList(key, codec);
    }

    public void cacheSet(String key, List<?> eleList, long liveTime) {
        RSet<Object> rSet = redissonClient.getSet(key, codec);
        rSet.addAll(eleList);
        rSet.expire(liveTime, TimeUnit.SECONDS);
    }

    public RSet<?> getSet(String key) {
        return redissonClient.getSet(key, codec);
    }

    public void cacheZSet(String key, Map<Object, Double> eleList, long liveTime) {
        RScoredSortedSet<Object> sortedSet = redissonClient.getScoredSortedSet(key, codec);
        sortedSet.addAll(eleList);
        sortedSet.expire(liveTime, TimeUnit.SECONDS);
    }

    public RScoredSortedSet<Object> getZSet(String key) {
        return redissonClient.getScoredSortedSet(key, codec);
    }


    public void clearCache(String pattern) {
        redissonClient.getKeys().deleteByPattern(pattern);
    }

}
