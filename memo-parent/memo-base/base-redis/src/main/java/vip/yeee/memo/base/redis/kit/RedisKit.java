package vip.yeee.memo.base.redis.kit;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import vip.yeee.memo.base.redis.constant.RedisConstant;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/7/10 15:35
 */
@Slf4j
@Component
public class RedisKit {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 缓存value操作
     */
    public void cacheValue(String k, String v, long time) {
        String key = RedisConstant.KEY_PREFIX_VALUE + k;
        try {
            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
            valueOps.set(key, v);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Throwable t) {
            log.error("缓存[" + key + "]失败, value[" + v + "]", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * 缓存value操作
     */
    public void cacheValue(String k, String v) {
        cacheValue(k, v, -1);
    }

    /**
     * 判断缓存是否存在
     */
    public boolean containsValueKey(String k) {
        return containsKey(RedisConstant.KEY_PREFIX_VALUE + k);
    }

    /**
     * 获取缓存
     */
    public String getValue(String k) {
        try {
            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
            return valueOps.get(RedisConstant.KEY_PREFIX_VALUE + k);
        } catch (Throwable t) {
            log.error("获取缓存失败key[" + RedisConstant.KEY_PREFIX_VALUE + k + "]", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * 移除缓存
     */
    public void removeValue(String k) {
        remove(RedisConstant.KEY_PREFIX_VALUE + k);
    }

    /**
     * 递增
     */
    public Long incrValue(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        try {
            return redisTemplate.opsForValue().increment(key, delta);
        } catch (Throwable t) {
            log.error("递增缓存失败key[" + key + "]", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * 递减
     */
    public Long decrValue(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        try {
            return redisTemplate.opsForValue().decrement(key, delta);
        } catch (Throwable t) {
            log.error("递减缓存失败key[" + key + "]", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * 缓存set
     */
    public void cacheSet(String k, Set<String> v, long time) {
        String key = RedisConstant.KEY_PREFIX_SET + k;
        try {
            SetOperations<String, String> setOps = redisTemplate.opsForSet();
            setOps.add(key, v.toArray(new String[v.size()]));
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Throwable t) {
            log.error("缓存[" + key + "]失败, value[" + v + "]", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * 缓存set
     */
    public void cacheSet(String k, Set<String> v) {
        cacheSet(k, v, -1);
    }

    /**
     * 缓存set操作
     */
    public void cacheSet(String k, String v, long time) {
        String key = RedisConstant.KEY_PREFIX_SET + k;
        try {
            SetOperations<String, String> valueOps = redisTemplate.opsForSet();
            valueOps.add(key, v);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Throwable t) {
            log.error("缓存[" + key + "]失败, value[" + v + "]", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * 缓存set
     */
    public void cacheSet(String k, String v) {
        cacheSet(k, v, -1);
    }

    /**
     * 判断缓存是否存在
     */
    public boolean containsSetKey(String k) {
        return containsKey(RedisConstant.KEY_PREFIX_SET + k);
    }

    /**
     * 获取缓存set数据
     */
    public Set<String> getSet(String k) {
        try {
            SetOperations<String, String> setOps = redisTemplate.opsForSet();
            return setOps.members(RedisConstant.KEY_PREFIX_SET + k);
        } catch (Throwable t) {
            log.error("获取set缓存失败key[" + RedisConstant.KEY_PREFIX_SET + k + "]", t);
            throw new RuntimeException(t);
        }
    }

    public void removeSet(String k) {
        remove(RedisConstant.KEY_PREFIX_SET + k);
    }

    /**
     * list缓存
     */
    public void cacheList(String k, String v, long time) {
        String key = RedisConstant.KEY_PREFIX_LIST + k;
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            listOps.leftPush(key, v);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Throwable t) {
            log.error("缓存[" + key + "]失败, value[" + v + "]", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * 缓存list
     */
    public void cacheList(String k, String v) {
        cacheList(k, v, -1);
    }

    /**
     * 缓存list
     */
    public void cacheList(String k, List<String> v, long time) {
        String key = RedisConstant.KEY_PREFIX_LIST + k;
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            listOps.rightPushAll(key, v);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Throwable t) {
            log.error("缓存[" + key + "]失败, value[" + v + "]", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * 缓存list
     */
    public void cacheList(String k, List<String> v) {
        cacheList(k, v, -1);
    }

    /**
     * 判断缓存是否存在
     */
    public boolean containsListKey(String k) {
        return containsKey(RedisConstant.KEY_PREFIX_LIST + k);
    }

    /**
     * 获取list缓存
     */
    public List<String> getList(String k, long start, long end) {
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            return listOps.range(RedisConstant.KEY_PREFIX_LIST + k, start, end);
        } catch (Throwable t) {
            log.error("获取list缓存失败key[" + RedisConstant.KEY_PREFIX_LIST + k + "]", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * 获取总条数, 可用于分页
     */
    public Long getListSize(String k) {
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            return listOps.size(RedisConstant.KEY_PREFIX_LIST + k);
        } catch (Throwable t) {
            log.error("获取list长度失败key[" + RedisConstant.KEY_PREFIX_LIST + k + "]", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * 移除list缓存
     */
    public String popOneOfList(String k) {
        String key = RedisConstant.KEY_PREFIX_LIST + k;
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            return listOps.rightPop(key);
        } catch (Throwable t) {
            log.error("移除list缓存失败key[" + RedisConstant.KEY_PREFIX_LIST + k + "]", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * 移除缓存
     */
    public void removeList(String k) {
        remove(RedisConstant.KEY_PREFIX_LIST + k);
    }

    /**
     * 缓存hash
     */
    public void cacheHash(String k, Map<Object, Object> v) {
        cacheHash(k, v, -1);
    }

    /**
     * 缓存hash
     */
    public void cacheHash(String k, Object hashKey, String v) {
        cacheHash(k, Collections.singletonMap(hashKey, v), -1);
    }

    /**
     * 缓存hash
     */
    public void cacheHash(String k, Map<Object, Object> v, long time) {
        String key = RedisConstant.KEY_PREFIX_HASH + k;
        try {
            HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
            operations.putAll(key, v);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Throwable t) {
            log.error("缓存[" + key + "]失败, value[" + v + "]", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * 判断缓存是否存在
     */
    public boolean containsHash(String k) {
        return containsKey(RedisConstant.KEY_PREFIX_HASH + k);
    }

    /**
     * 判断缓存是否存在
     */
    public boolean containsHashKey(String k, Object hashKey) {
        String key = RedisConstant.KEY_PREFIX_HASH + k;
        try {
            HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
            return operations.hasKey(key, hashKey);
        } catch (Throwable t) {
            log.error("key[" + key + "], hashKey[" + hashKey + "]", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * 获取hash缓存
     */
    public Map<Object, Object> getHash(String k) {
        String key = RedisConstant.KEY_PREFIX_HASH + k;
        try {
            HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
            return operations.entries(key);
        } catch (Throwable t) {
            log.error("获取hash缓存失败key[" + RedisConstant.KEY_PREFIX_LIST + k + "]", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * 获取hash缓存
     */
    public Object getHashItem(String k, String hashKey) {
        String key = RedisConstant.KEY_PREFIX_HASH + k;
        try {
            HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
            return operations.get(key, hashKey);
        } catch (Throwable t) {
            log.error("获取hash缓存失败key[" + RedisConstant.KEY_PREFIX_LIST + k + "]", t);
            throw new RuntimeException(t);
        }
    }

    public Long incrHashItem(String k, String hashKey, long delta) {
        String key = RedisConstant.KEY_PREFIX_HASH + k;
        try {
            HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
            return operations.increment(key, hashKey, delta);
        } catch (Throwable t) {
            log.error("递增hash缓存失败key[" + RedisConstant.KEY_PREFIX_LIST + k + "]", t);
            throw new RuntimeException(t);
        }
    }

    public Long decrHashItem(String k, String hashKey, long delta) {
        return incrHashItem(k, hashKey, delta * (-1));
    }

    /**
     * 移除缓存
     */
    public void removeHash(String k) {
        remove(RedisConstant.KEY_PREFIX_HASH + k);
    }

    /**
     * 移除缓存
     */
    public void removeHashKey(String k, Object... hashKey) {
        String key = RedisConstant.KEY_PREFIX_HASH + k;
        try {
            HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
            operations.delete(key, hashKey);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }






    /**
     * 缓存ZSet
     */
    public void cacheZSet(String k, String v, double score) {
        cacheZSet(k, v, score, -1);
    }

    /**
     * 缓存ZSet
     */
    public void cacheZSet(String k, String v, double score, long time) {
        String key = RedisConstant.KEY_PREFIX_ZSET + k;
        try {
            ZSetOperations<String, String> operations = redisTemplate.opsForZSet();
            operations.add(key, v, score);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Throwable t) {
            log.error("缓存[" + key + "]失败, value[" + v + "]", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * 判断缓存是否存在
     */
    public boolean containsZSet(String k) {
        return containsKey(RedisConstant.KEY_PREFIX_ZSET + k);
    }

    /**
     * 获取hash缓存
     */
    public Set<String> getZSet(String k, long start, long end) {
        String key = RedisConstant.KEY_PREFIX_ZSET + k;
        try {
            ZSetOperations<String, String> operations = redisTemplate.opsForZSet();
            return operations.range(key, start, end);
        } catch (Throwable t) {
            log.error("获取zset缓存失败key[" + RedisConstant.KEY_PREFIX_LIST + k + "]", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * 获取hash缓存
     */
    public Double getZSetScore(String k, String value) {
        String key = RedisConstant.KEY_PREFIX_HASH + k;
        try {
            ZSetOperations<String, String> operations = redisTemplate.opsForZSet();
            return operations.score(key, value);
        } catch (Throwable t) {
            log.error("获取zset缓存失败key[" + RedisConstant.KEY_PREFIX_LIST + k + "]", t);
            throw new RuntimeException(t);
        }
    }

    public Double incrZSetScore(String k, String value, double delta) {
        String key = RedisConstant.KEY_PREFIX_HASH + k;
        try {
            ZSetOperations<String, String> operations = redisTemplate.opsForZSet();
            return operations.incrementScore(key, value, delta);
        } catch (Throwable t) {
            log.error("递增zset缓存失败key[" + RedisConstant.KEY_PREFIX_LIST + k + "]", t);
            throw new RuntimeException(t);
        }
    }

    public Double decrZSetScore(String k, String value, double delta) {
        return incrZSetScore(k, value, delta * (-1));
    }

    /**
     * 移除缓存
     */
    public void removeZSet(String k) {
        remove(RedisConstant.KEY_PREFIX_ZSET + k);
    }

    /**
     * 移除缓存
     */
    public void removeZSetValue(String k, Object... value) {
        String key = RedisConstant.KEY_PREFIX_HASH + k;
        try {
            ZSetOperations<String, String> operations = redisTemplate.opsForZSet();
            operations.remove(key, value);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
     * 移除缓存
     */
    public void remove(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Throwable t) {
            log.error("移除缓存失败key[" + key + "]", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * 根据前缀移除缓存
     */
    public void removeByPrefix(String prefix) {
        try {
            Set<String> keys = redisTemplate.keys(prefix + "*");
            if (CollectionUtil.isNotEmpty(keys)) {
                redisTemplate.delete(keys);
            }
        } catch (Throwable t) {
            log.error("移除缓存失败前缀key[" + prefix + "]", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * 功能描述: 设置过期时间
     */
    public void expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Throwable t) {
            log.error("过期时间设置失败key[" + key + "]", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * 功能描述: 查看过期时间
     */
    public Long getExpire(String key) {
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Throwable t) {
            log.error("过期时间获取失败key[" + key + "]", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * 查找匹配key (注意添加前缀)
     */
    public List<String> scan(String pattern) {
        ScanOptions options = ScanOptions.scanOptions().match(pattern).build();
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        RedisConnection rc = Objects.requireNonNull(factory).getConnection();
        Cursor<byte[]> cursor = rc.scan(options);
        List<String> result = new ArrayList<>();
        while (cursor.hasNext()) {
            result.add(new String(cursor.next()));
        }
        try {
            RedisConnectionUtils.releaseConnection(rc, factory);
        } catch (Throwable t) {
            log.error("过期时间获取失败key[" + pattern + "]", t);
        }
        return result;
    }

    public boolean containsKey(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Throwable t) {
            log.error("判断缓存存在失败key[" + key + "]", t);
            throw new RuntimeException(t);
        }
    }

    public boolean canRepeatOpr(String redisKey, long expire) {
        try {
            Boolean res = redisTemplate
                    .opsForValue()
                    .setIfAbsent("YEEE:CAN_REPEAT_OPR:" + redisKey, "1", expire, TimeUnit.SECONDS);
            return Boolean.TRUE.equals(res);
        } catch (Exception e) {
            log.error("判断缓存存在失败key[" + redisKey + "]", e);
            return true;
        }
    }

}
