package vip.yeee.memo.base.redis.utils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.domain.geo.BoundingBox;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Component;
import vip.yeee.memo.base.redis.constant.RedisConstant;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/7/10 15:35
 */
@Slf4j
@Component
public class RedisUtil {

//    @Resource
//    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;

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
    public Long removeList(String k, long count, Object value) {
        String key = RedisConstant.KEY_PREFIX_LIST + k;
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            return listOps.remove(key, count, value);
        } catch (Throwable t) {
            log.error("移除list缓存失败key[" + RedisConstant.KEY_PREFIX_LIST + k + "]", t);
            throw new RuntimeException(t);
        }
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
    public Long removeZSetRangeByScore(String k, double min, double max) {
        String key = RedisConstant.KEY_PREFIX_HASH + k;
        try {
            ZSetOperations<String, String> operations = redisTemplate.opsForZSet();
            return operations.removeRangeByScore(key, min, max);
        } catch (Throwable t) {
            log.error("移除zset缓存失败key[" + RedisConstant.KEY_PREFIX_LIST + k + "]", t);
            throw new RuntimeException(t);
        }
    }

    public Long getZSetSize(String k) {
        String key = RedisConstant.KEY_PREFIX_HASH + k;
        try {
            ZSetOperations<String, String> operations = redisTemplate.opsForZSet();
            return operations.size(key);
        } catch (Throwable t) {
            log.error("获取缓存失败key[" + RedisConstant.KEY_PREFIX_LIST + k + "]", t);
            throw new RuntimeException(t);
        }
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

    /**
     * 添加地理位置
     */
    public void addGeoLocations(String key, Map<String, Point> locations, long expire) {
        try {
            redisTemplate.opsForGeo()
                    .add(key, locations);
            if (expire > 0) {
                redisTemplate.expire(key, expire, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error("缓存GEO失败，key[" + key + "]", e);
            throw new RuntimeException(e);
        }
    }

    public void addGeoLocations(String key, Map<String, Point> locations) {
        addGeoLocations(key, locations, -1);
    }


    public void addGeoLocation(String key, double longitude, double latitude, String member, long expire) {
        addGeoLocations(key, Collections.singletonMap(member, new Point(longitude, latitude)), expire);
    }

    public void addGeoLocation(String key, double longitude, double latitude, String member) {
        addGeoLocation(key, longitude, latitude, member, -1);
    }

    /**
     * 查询地理位置
     */
    public List<Point> getGeoPositions(String key, List<String> members) {
        try {
            return redisTemplate.opsForGeo()
                    .position(key, members.toArray(new String[0]));
        } catch (Exception e) {
            log.error("获取GEO失败，key[" + key + "]", e);
            throw new RuntimeException(e);
        }
    }

    public Point getGeoPosition(String key, String member) {
        List<Point> positions = getGeoPositions(key, Collections.singletonList(member));
        if (CollectionUtil.isEmpty(positions)) {
            return null;
        }
        return positions.get(0);
    }

    /**
     * 查询地理距离
     */
    public Distance getGeoDistance(String key, String member1, String member2) {
        try {
            return redisTemplate.opsForGeo()
                    .distance(key, member1, member2);
        } catch (Exception e) {
            log.error("获取GEO距离失败，key[" + key + "]", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询半径范围坐标
     */
    public GeoResults<RedisGeoCommands.GeoLocation<String>> getGeoRadius(String key, Point center, Distance radius) {
        try {
            RedisGeoCommands.GeoRadiusCommandArgs commandArgs = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();
            commandArgs.includeCoordinates();
            commandArgs.includeDistance();
            return redisTemplate.opsForGeo()
                    .radius(key, new Circle(center, radius), commandArgs);
        } catch (Exception e) {
            log.error("获取GEO半径内坐标失败，key[" + key + "]", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询半径范围坐标
     */
    public GeoResults<RedisGeoCommands.GeoLocation<String>> getGeoRadius(String key, String member, Distance radius) {
        try {
            RedisGeoCommands.GeoRadiusCommandArgs commandArgs = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();
            commandArgs.includeCoordinates();
            commandArgs.includeDistance();
            return redisTemplate.opsForGeo()
                    .radius(key, member, radius, commandArgs);
        } catch (Exception e) {
            log.error("获取GEO半径内坐标失败，key[" + key + "]", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询半径范围坐标 redis6.2
     */
    public GeoResults<RedisGeoCommands.GeoLocation<String>> getGeoByCircle(String key, Point center, Distance radius) {
        try {
            return redisTemplate.opsForGeo()
                    .search(key, new Circle(center, radius));
        } catch (Exception e) {
            log.error("获取GEO半径内坐标失败，key[" + key + "]", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询半径范围坐标 redis6.2
     */
    public GeoResults<RedisGeoCommands.GeoLocation<String>> getGeoByCircle(String key, String member, Distance radius) {
        try {
            return redisTemplate.opsForGeo()
                    .search(key, GeoReference.fromMember(member), radius);
        } catch (Exception e) {
            log.error("获取GEO半径内坐标失败，key[" + key + "]", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询矩形范围坐标 redis6.2
     */
    public GeoResults<RedisGeoCommands.GeoLocation<String>> getGeoByBox(String key, Point point, Distance width, Distance height) {
        try {
            return redisTemplate.opsForGeo()
                    .search(key, GeoReference.fromCoordinate(point), new BoundingBox(width, height));
        } catch (Exception e) {
            log.error("获取GEO矩形内坐标失败，key[" + key + "]", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询矩形范围坐标 redis6.2
     */
    public GeoResults<RedisGeoCommands.GeoLocation<String>> getGeoByBox(String key, String member, Distance width, Distance height) {
        try {
            return redisTemplate.opsForGeo()
                    .search(key, GeoReference.fromMember(member), new BoundingBox(width, height));
        } catch (Exception e) {
            log.error("获取GEO矩形内坐标失败，key[" + key + "]", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加流 redis5.0
     */
    public RecordId addStream(String key, String message) {
        try {
            return redisTemplate.opsForStream()
                    .add(StreamRecords.newRecord().in(key).ofObject(message));
        } catch (Exception e) {
            log.error("添加Stream失败，key[" + key + "]", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建消费者组 redis5.0
     */
    public void createGroup(String key, String group) {
        try {
            redisTemplate.opsForStream().createGroup(key, group);
        } catch (Exception e) {
            if (e.getMessage().contains("already exists")) {
                return;
            }
            log.error("创建Stream消费组失败，key[" + key + "]", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取流 redis5.0
     */
    public List<MapRecord<String, String, String>> readStream(String key, String consumerName, String group) {
        try {
            StreamReadOptions readOptions = StreamReadOptions.empty()
                    .block(Duration.ofSeconds(10))
                    .count(20)
                    .autoAcknowledge();
            return redisTemplate.<String, String>opsForStream()
                    .read(Consumer.from(group, consumerName), readOptions, StreamOffset.create(key, ReadOffset.lastConsumed()));
        } catch (Exception e) {
            log.error("获取Stream失败，key[" + key + "]", e);
            throw new RuntimeException(e);
        }
    }

}
