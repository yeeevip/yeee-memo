package com.yeee.jedis.simple;

import com.yeee.jedis.JedisRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.UUID;

/**
 * @author yeeeee
 * @since  2021/12/22 11:24
 */
@Slf4j
@Data
public class JedisCommandLock {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXISTS = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    private static final Long RELEASE_SUCCESS = 1L;

    // requestId：持有此id的才可以unlock；expireTime：防止死锁
    public static boolean tryGetLock(Jedis jedis, String lockKey, String requestId, long expireTime) {
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXISTS, SET_WITH_EXPIRE_TIME, expireTime);
        return LOCK_SUCCESS.equals(result);
    }

    public static boolean releaseLock(Jedis jedis, String lockKey, String requestId) {
        // eval命令执行Lua代码的时候，Lua代码将被当成一个命令去执行 [原子性]
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object res = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        return RELEASE_SUCCESS.equals(res);
    }

    public static void main(String[] args) {

        Jedis jedis = JedisRepository.getJedis();

        String lockKey = "test";
        String requestId = UUID.randomUUID().toString();

        // lock
        boolean res = JedisCommandLock.tryGetLock(jedis, lockKey, requestId, 100000);
        log.info("tryGetLock ：" + res);

        // unlock
        boolean res2 = JedisCommandLock.releaseLock(jedis, lockKey, requestId);
        log.info("releaseLock : " + res2);

    }

}
