package vip.yeee.memo.demo.distribute.lock.redisson.example;

import org.redisson.RedissonMultiLock;
import org.redisson.RedissonRedLock;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * description ...
 *
 * @author https://www.yeee.vip
 * @since 2021/12/28 16:25
 */
public class AllTypeLockUseExample {

    // 三个不同的【主节点Master】实例
    @Resource
    @Qualifier("redissonClient1")
    private RedissonClient redissonClient1;
    @Resource
    @Qualifier("redissonClient2")
    private RedissonClient redissonClient2;
    @Resource
    @Qualifier("redissonClient3")
    private RedissonClient redissonClient3;

    /**
     * 可重入锁-sync
     * @author https://www.yeee.vip
     * @since 2021/12/28 16:37
     */
    public void testReentrantLock() {
        RLock lock = redissonClient1.getLock("anyLock");
        // 1.最常用的使用方法
        lock.lock();
        // 2.支持过期解锁功能，10s以后自动解锁，无需调用unlock()方法手动解锁
        lock.lock(10, TimeUnit.SECONDS);
        try {
            // 3.尝试加锁，最多等待3s，上锁以后10s解锁
            boolean res = lock.tryLock(3, 10, TimeUnit.SECONDS);
            if (res) {
                // do your business
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 可重入锁-async
     * @author https://www.yeee.vip
     * @since 2021/12/28 16:37
     */
    public void testAsyncReentrantLock() {
        RLock lock = redissonClient1.getLock("anyLock");
        try {
            lock.lockAsync().get();
            lock.lockAsync(10, TimeUnit.SECONDS).get();
            RFuture<Boolean> res = lock.tryLockAsync(3, 10, TimeUnit.SECONDS);
            if (res.get()) {
                // do your business
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 公平锁 / 优先分配锁给先发出请求的线程
     * @author https://www.yeee.vip
     * @since 2021/12/28 16:38
     */
    public void testFairLock() {
        RLock fairLock = redissonClient1.getFairLock("anyLock");
        try {
            // 最常见的使用方法
            fairLock.lock();
            //
            fairLock.lock(10, TimeUnit.SECONDS);
            //
            boolean res = fairLock.tryLock(100, 10, TimeUnit.SECONDS);

            // async
            fairLock.lockAsync();
            fairLock.lockAsync(10, TimeUnit.SECONDS);
            fairLock.tryLockAsync(3, 10, TimeUnit.SECONDS);
            RFuture<Boolean> res2 = fairLock.tryLockAsync(3, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            fairLock.unlock();
        }
    }

    /**
     * 可以使用【多个redisson高可用master】实例
     * 联锁 / 将多个RLock对象关联为一个联锁 [所有的RLock上锁成功才算成功]
     *
     * 一般场景就是某些业务需要同时锁住多个资源，使用【一个redisson实例】
     * 也可以将多个不同master redisson实例加锁，性能比起redLock低很多
     * 比如说，在一个下单的业务场景中，同时需要锁定订单、库存、商品，基于这种需要锁多种资源的场景中
     * @author https://www.yeee.vip
     * @since 2021/12/28 16:43
     */
    public void testMultiLock() {
        RLock lock1 = redissonClient1.getLock("lock1");
        RLock lock2 = redissonClient1.getLock("lock2");
        RLock lock3 = redissonClient1.getLock("lock3");
        RedissonMultiLock multiLock = new RedissonMultiLock(lock1, lock2, lock3);
        try {
            // 同时加锁：lock1 lock2 lock3, 所有的锁都上锁成功才算成功。
            multiLock.lock();
            // 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
            boolean res = multiLock.tryLock(100, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            multiLock.unlock();
        }
    }

    /**
     * #######【extends RedissonMultiLock，[扩展了一个master节点到多个master节点的redisson实例]，来解决主从架构锁失效问题】########
     * 可以使用【多个redisson高可用master】实例
     * ---
     * 解决主从架构锁失效问题：就是说在主从架构系统中，线程A从master中获取到分布式锁
     * ，数据还未同步到slave中时master就挂掉了，slave成为新的master
     * ，其它线程从新的master获取锁也成功了，就会出现并发安全问题
     * ---
     * 红锁 / 将多个RLock对象关联为一个联锁 [大部分redis节点上加锁成功才算成功 （n / 2 + 1）]
     * @author https://www.yeee.vip
     * @since 2021/12/28 16:52
     */
    public void testRedLock(){
        RLock lock1 = redissonClient1.getLock("LOCK:ID:KEY");
        RLock lock2 = redissonClient2.getLock("LOCK:ID:KEY");
        RLock lock3 = redissonClient3.getLock("LOCK:ID:KEY");
        RedissonRedLock lock = new RedissonRedLock(lock1, lock2, lock3);
        try {
            // 同时加锁：lock1 lock2 lock3, 红锁在大部分节点上加锁成功就算成功。
            lock.lock();
            // 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
            boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 读写锁
     * @author https://www.yeee.vip
     * @since 2021/12/28 16:54
     */
    public void testReadWriteLock() {
        RReadWriteLock readWriteLock = redissonClient1.getReadWriteLock("anyLock");
        RLock readLock = readWriteLock.readLock();
        RLock writeLock = readWriteLock.writeLock();
        try {
            // 最常见的使用方法
            readLock.lock();
            // 或
            writeLock.lock();
            // 支持过期解锁功能
            // 10秒钟以后自动解锁
            // 无需调用unlock方法手动解锁
            readLock.lock(10, TimeUnit.SECONDS);
            // 或
            writeLock.lock(10, TimeUnit.SECONDS);
            // 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
            boolean res = readLock.tryLock(100, 10, TimeUnit.SECONDS);
            // 或
            boolean res2 = writeLock.tryLock(100, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
            writeLock.unlock();
        }
    }

    /**
     * 信号量
     * @author https://www.yeee.vip
     * @since 2021/12/28 16:59
     */
    public void testSemaphore() {
        RSemaphore semaphore;
        {
            semaphore = redissonClient1.getSemaphore("semaphore");
            semaphore.trySetPermits(100);
        }
        try {
            semaphore.acquire();
            //或
            semaphore.acquireAsync();
            semaphore.acquire(23);
            semaphore.tryAcquire();
            //或
            semaphore.tryAcquireAsync();
            semaphore.tryAcquire(23, TimeUnit.SECONDS);
            //或
            semaphore.tryAcquireAsync(23, TimeUnit.SECONDS);
            semaphore.release(10);
            semaphore.release();
            //或
            semaphore.releaseAsync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 可过期性信号量
     * @author https://www.yeee.vip
     * @since 2021/12/28 17:18
     */
    public void testPermitExpirableSemaphore() {
        RPermitExpirableSemaphore semaphore;
        {
            semaphore = redissonClient1.getPermitExpirableSemaphore("mySemaphore");
            semaphore.trySetPermits(100);
        }
        try {
            String permitId = semaphore.acquire();
            // 获取一个信号，有效期只有2秒钟。
            String permitId2 = semaphore.acquire(2, TimeUnit.SECONDS);
            // ...
            semaphore.release(permitId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 闭锁 / 倒数闩
     * @author https://www.yeee.vip
     * @since 2021/12/28 17:31
     */
    public void testCountDownLatch() {
        RCountDownLatch countDownLatch = redissonClient1.getCountDownLatch("anyCountDownLatch");
        countDownLatch.trySetCount(1);

        // 在其他线程或者JVM里
        {
            RCountDownLatch latch = redissonClient1.getCountDownLatch("anyCountDownLatch");
            latch.countDown();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
