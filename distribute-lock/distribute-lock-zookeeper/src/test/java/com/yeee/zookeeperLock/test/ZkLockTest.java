package com.yeee.zookeeperLock.test;

import com.yeee.zookeeper.lock.simple.ZkLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * description ...
 *
 * @author yeeeee
 * @since 2021/12/30 17:06
 */
@Slf4j
public class ZkLockTest {

    static int count = 0;
    @Test
    public void testLock() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        CountDownLatch latch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
                ZkLock lock = new ZkLock();
                try {
                    lock.lock();
                    for (int j = 0; j < 100; j ++) {
                        count++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
                return count;
            }, executorService);
            future.thenAccept(count -> {
                log.info("线程 {} 执行结束，累加 count = {}", Thread.currentThread().getId(), count);
                latch.countDown();
            });
        }
        latch.await();
        log.info("所有线程任务最终累加 count = {}", count);
    }

    @Test
    public void testLockSimple() {
        ZkLock lock = new ZkLock();
        try {
            lock.lock();
            for (int j = 0; j < 100; j ++) {
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        log.info("count = {}", count);
    }

}
