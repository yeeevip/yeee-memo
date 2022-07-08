package vip.yeee.memo.integrate.distribute.lock.jedis.lua.impl_juc_lock;

import vip.yeee.memo.integrate.distribute.lock.jedis.JedisRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * description ...
 *
 * @author yeeeee
 * @since 2021/12/22 17:28
 */
@Slf4j
public class JedisLock implements Lock {

    private static final int DEFAULT_TIMEOUT = 2000;
    private static final Long LOCKED = 1L;
    private static final Long UNLOCKED = 1L;
    private static final Long WAIT_GAT = 200L;

    private volatile boolean isLocked;
    private Thread thread;

    Jedis jedis;
    String lockKey;
    String requestId; // lockValue 锁的value，代表线程的uuid

    /**
     * 默认2000ms
     */
    long expire = 2000L;

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

    @Override
    public void lock() {
        // @see com.yeee.jedis.lua.simp.JedisLock
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        // @see com.yeee.jedis.lua.simp.JedisLock
        return false;
    }

    @SneakyThrows
    @Override
    public boolean tryLock(long time, TimeUnit unit) {

        // 本地可重入
        if (isLocked && thread == Thread.currentThread()) {
            return true;
        }

        long millsToWait = unit != null ? unit.toMillis(time) : DEFAULT_TIMEOUT;
        long startMills = System.currentTimeMillis();

        boolean localLocked = false;

        int turn = 1;
        while (!localLocked) {
            localLocked = this.lockInner();
            if (!localLocked) {
                millsToWait = millsToWait - (System.currentTimeMillis() - startMills);
                startMills = System.currentTimeMillis();
                if (millsToWait > 0L) {
                    // 还没有超时
                    TimeUnit.MILLISECONDS.sleep(WAIT_GAT);
                    log.info("睡眠一下，重新开始，turn：{}，剩余时间：{}", turn++, millsToWait);
                } else {
                    log.info("抢锁超时");
                    return false;
                }
            } else {
                thread = Thread.currentThread();
                isLocked = true;
                localLocked = true;
            }
        }
        return isLocked;
    }

    public boolean lockInner() throws Exception {
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

    @SneakyThrows
    @Override
    public void unlock() {
        if (lockKey == null || requestId == null) {
            return;
        }
        try {
            jedis.eval(unlockScript, 2, lockKey, requestId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("释放锁失败");
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    public static void main(String[] args) {

        Jedis jedis = JedisRepository.getJedis();

        String lockKey = "test";
        String requestId = UUID.randomUUID().toString();

        JedisLock jedisLock = new JedisLock(jedis, lockKey, requestId);

        // lock
        boolean res1 = jedisLock.tryLock(5, TimeUnit.SECONDS);
        log.info("lock：" + res1);

        // unlock
       //jedisLock.unlock();

    }

}
