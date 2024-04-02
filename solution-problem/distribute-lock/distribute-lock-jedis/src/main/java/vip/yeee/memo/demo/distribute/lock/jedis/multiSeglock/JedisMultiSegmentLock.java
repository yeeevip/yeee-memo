package vip.yeee.memo.demo.distribute.lock.jedis.multiSeglock;

import cn.hutool.core.util.RandomUtil;
import vip.yeee.memo.demo.distribute.lock.jedis.lua.impl_juc_lock.JedisLock;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * description ...
 *
 * @author https://www.yeee.vipe
 * @since 2021/12/28 18:37
 */
@Slf4j
public class JedisMultiSegmentLock implements Lock {

    public static final int NO_SEG = -1;
    // 拿到锁的线程
    private Thread thread;

    // 拿到锁的状态
    private volatile boolean isLocked = false;

    // 段锁
    private final int segAmount;

    private static final int DEFAULT_TIMEOUT = 2000;
    private static final Long WAIT_GAT = Long.valueOf(100);

    // 内部的锁
    JedisLock[] innerLocks = null;

    // 被锁住的分段
    int segmentIndexLocked = NO_SEG;

    //
    long expire = 2000L;
    int segmentIndex = 0;

    public JedisMultiSegmentLock(String lockKey, String requestId, int segAmount, Jedis jedis) {
        this.segAmount = segAmount;
        innerLocks = new JedisLock[segAmount];
        for (int i = 0; i < this.segAmount; i++) {
            // 每一个分段，加上一个编号
            String innerLockKey = lockKey + ":" + i;
            innerLocks[i] = new JedisLock(jedis, innerLockKey, requestId);
        }
        segmentIndex = RandomUtil.randomInt(this.segAmount);
    }

    @Override
    public void lock() {
        throw new IllegalStateException("方法 'lock' 尚未实现!");
    }

    /**
     * 获取一个分布式锁 , 超时则返回失败 | 有问题！！！
     *
     * @return 获锁成功 - true | 获锁失败 - false
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {

        // 本地可重入
        if (isLocked && thread == Thread.currentThread()) {
            return true;
        }

        long millsToWait = unit != null ? unit.toMillis(time) : DEFAULT_TIMEOUT;
        long startMills = System.currentTimeMillis();

        boolean localLocked = false;

        int turn = 1;

        JedisLock innerLock = innerLocks[segmentIndex];

        while (!localLocked) {
            localLocked = innerLock.tryLock(expire, TimeUnit.SECONDS);
            if (!localLocked) {
                millsToWait = millsToWait - (System.currentTimeMillis() - startMills);
                startMills = System.currentTimeMillis();
                if (millsToWait > 0L) {
                    // 还没有超时
                    TimeUnit.MILLISECONDS.sleep(WAIT_GAT);
                    log.info("睡眠一下，重新开始，turn：{}，剩余时间：{}", turn++, millsToWait);

                    segmentIndex++;
                    if (segmentIndex >= this.segAmount) {
                        segmentIndex = 0;
                    }
                    innerLock = innerLocks[segmentIndex];
                } else {
                    log.info("抢锁超时");
                    return false;
                }
            } else {
                thread = Thread.currentThread();
                segmentIndexLocked = segmentIndex;
                isLocked = true;
                localLocked = true;
            }
        }
        return isLocked;

    }

    @Override
    public void unlock() {

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        throw new IllegalStateException("方法 'lockInterruptibly' 尚未实现!");
    }

    @Override
    public boolean tryLock() {
        throw new IllegalStateException("方法 'tryLock' 尚未实现!");
    }

    @Override
    public Condition newCondition() {
        throw new IllegalStateException("方法 'newCondition' 尚未实现!");
    }

    @Getter
    @Setter
    static class InnerLock {
        private String innerLockKey;
        private String requestId;
        public InnerLock(String innerLockKey, String requestId) {
            this.innerLockKey = innerLockKey;
            this.requestId = requestId;
        }
    }

}
