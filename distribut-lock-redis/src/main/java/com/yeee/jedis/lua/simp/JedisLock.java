package com.yeee.jedis.lua.simp;

import com.yeee.jedis.JedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.RedisScript;
import redis.clients.jedis.Jedis;

import java.util.UUID;

/**
 * description ...
 *
 * @author yeeeee
 * @since 2021/12/22 14:48
 */
@Slf4j
public class JedisLock {

    private static final Long LOCKED = 1L;
    private static final Long UNLOCKED = 1L;

    Jedis jedis;
    String lockKey;
    String requestId; // lockValue 锁的value，代表线程的uuid

    /**
     * 默认2000ms
     */
    long expire = 20000L;

    private final RedisScript<String> lockScript;
    private final RedisScript<String> unlockScript;

    public JedisLock(Jedis jedis, String lockKey, String requestId) {
        this.jedis = jedis;
        this.lockKey = lockKey;
        this.requestId = requestId;
        lockScript = RedisScript.of(new ClassPathResource("script/lock.lua"));
        unlockScript = RedisScript.of(new ClassPathResource("script/unlock.lua"));
    }

    /**
     * 有返回值的抢夺锁
     */
    public boolean lock() throws Exception {
        if (lockKey == null) {
            return false;
        }
        try {
            Long res = (Long) jedis.eval(lockScript.getScriptAsString(), 3, lockKey, requestId, String.valueOf(expire));
            return res != null && res.equals(LOCKED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("抢锁失败");
        }
    }

    /**
     * 释放锁
     */
    public boolean unlock() throws Exception {
        if (lockKey == null || requestId == null) {
            return false;
        }
        try {
                Long res = (Long) jedis.eval(unlockScript.getScriptAsString(), 2, lockKey, requestId);
                return res != null && res.equals(UNLOCKED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("释放锁失败");
        }
    }

    public static void main(String[] args) throws Exception {

        Jedis jedis = JedisRepository.getJedis();

        String lockKey = "test";
        String requestId = UUID.randomUUID().toString();

        JedisLock innerLock = new JedisLock(jedis, lockKey, requestId);

        // lock
        boolean res1 = innerLock.lock();
        log.info("lock：" + res1);

        // unlock
        boolean res2 = innerLock.unlock();
        log.info("unlock：" + res2);

    }

}
