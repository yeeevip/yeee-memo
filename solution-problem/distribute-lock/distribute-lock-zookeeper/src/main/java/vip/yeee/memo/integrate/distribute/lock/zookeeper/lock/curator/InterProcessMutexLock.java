package vip.yeee.memo.integrate.distribute.lock.zookeeper.lock.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.*;

/**
 * 使用轮子 org.apache.curator.framework.recipes.locks.InterProcessMutex
 * @author yeeeee
 * @since 2021/12/30 18:07
 */
@Slf4j
public class InterProcessMutexLock {

    static int count = 0;

    public void testZkMutex() throws InterruptedException {

        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1", new RetryNTimes(10, 5000));
        client.start();

        int taskCount = 100;

        ThreadPoolExecutor executors =  new ThreadPoolExecutor(10, 10
                , 5, TimeUnit.SECONDS
                , new LinkedBlockingQueue<>(100)
                , Executors.defaultThreadFactory()
                , new ThreadPoolExecutor.AbortPolicy());

        CountDownLatch latch = new CountDownLatch(taskCount);

        for (int i = 0; i < taskCount; i++) {
            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
                InterProcessMutex lock = new InterProcessMutex(client, "/mylock");
                try {
                    lock.acquire();
                    for (int j = 0; j < 100; j++) {
                        count++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        lock.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return count;
            }, executors);
            future.thenAccept(count -> {
                log.info("线程 {} 执行完毕，count = {}", Thread.currentThread().getId(), count);
                latch.countDown();
            });
        }
        latch.await();
        log.info("所有线程执行完毕，count = {}", count);
    }

    public static void main(String[] args) throws InterruptedException {
        new InterProcessMutexLock().testZkMutex();
    }

}
