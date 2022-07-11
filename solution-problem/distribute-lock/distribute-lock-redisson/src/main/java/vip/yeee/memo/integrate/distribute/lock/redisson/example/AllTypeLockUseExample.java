package vip.yeee.memo.integrate.distribute.lock.redisson.example;

import lombok.RequiredArgsConstructor;
import org.redisson.RedissonMultiLock;
import org.redisson.RedissonRedLock;
import org.redisson.api.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * description ...
 *
 * @author yeeeee
 * @since 2021/12/28 16:25
 */
@RequiredArgsConstructor
public class AllTypeLockUseExample {

    private final RedissonClient redissonClient1;

    private final RedissonClient redissonClient2;

    private final RedissonClient redissonClient3;

    /**
     * 可重入锁-sync
     * @author yeeeeee
     * @since 2021/12/28 16:37
     */
    public void testReentrantLock() {
        RLock lock = redissonClient1.getLock("anyLock");
        try {
            // 1.最常用的使用方法
            lock.lock();
            // 2.支持过期解锁功能，10s以后自动解锁，无需调用unlock()方法手动解锁
            lock.lock(10, TimeUnit.SECONDS);
            // 3.尝试加锁，最多等待3s，上锁以后10s解锁
            boolean res = lock.tryLock(3, 10, TimeUnit.SECONDS);
            if (res) {
                // do your business
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 可重入锁-async
     * @author yeeeeee
     * @since 2021/12/28 16:37
     */
    public void testAsyncReentrantLock() {
        RLock lock = redissonClient1.getLock("anyLock");
        try {
            lock.lockAsync();
            lock.lockAsync(10, TimeUnit.SECONDS);
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
     * @author yeeeeee
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
     * 联锁 / 将多个RLock对象关联为一个联锁 [所有的redis节点上都上锁成功才算成功]
     * @author yeeeeee
     * @since 2021/12/28 16:43
     */
    public void testMultiLock() {
        RLock lock1 = redissonClient1.getLock("lock1");
        RLock lock2 = redissonClient2.getLock("lock2");
        RLock lock3 = redissonClient3.getLock("lock3");
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
     * 红锁 / 将多个RLock对象关联为一个联锁 [大部分redis节点上加锁成功才算成功 （n / 2 + 1）]
     * @author yeeeeee
     * @since 2021/12/28 16:52
     */
    public void testRedLock(){
        RLock lock1 = redissonClient1.getLock("lock1");
        RLock lock2 = redissonClient2.getLock("lock2");
        RLock lock3 = redissonClient3.getLock("lock3");
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
     * @author yeeeeee
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
     * @author yeeeeee
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
     * @author yeeeeee
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
     * @author yeeeeee
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
