package vip.yeee.memo.demo.distribute.lock.jedis.lua.simp;

import vip.yeee.memo.demo.distribute.lock.jedis.JedisRepository;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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

    // 加锁lua脚本
    private static final String lockScript = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("script/lock.lua"))))
            .lines().collect(Collectors.joining(System.lineSeparator()));
    // 释放锁脚本
    private static final String unlockScript = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("script/unlock.lua"))))
            .lines().collect(Collectors.joining(System.lineSeparator()));

    public JedisLock(Jedis jedis, String lockKey, String requestId) {
        this.jedis = jedis;
        this.lockKey = lockKey;
        this.requestId = requestId;
    }

    /**
     * 有返回值的抢夺锁
     */
    public boolean lock() throws Exception {
        if (lockKey == null) {
            return false;
        }
        try {
            Long res = (Long) jedis.eval(lockScript, 3, lockKey, requestId, String.valueOf(expire));
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
                Long res = (Long) jedis.eval(unlockScript, 2, lockKey, requestId);
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
