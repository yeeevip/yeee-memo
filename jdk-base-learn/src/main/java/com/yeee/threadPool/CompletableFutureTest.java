package com.yeee.threadPool;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * description ...
 *
 * @author yeeeee
 * @since 2021/12/28 18:12
 */
public class CompletableFutureTest {

    /**
     * 使用 CompletableFuture 和 CountDownLatch 进行并发回调
     * @author yeeeeee
     * @since 2021/12/28 18:13
     */
    public void testMultiCallBack() {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        // 批量异步
        // ExecutorService executor = ThreadUtil.getIoIntenseTargetThreadPool();
        ExecutorService executor = Executors.newFixedThreadPool(5);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
                long tid = Thread.currentThread().getId();
                try {
                    System.out.println("线程" + tid + "开始了,模拟一下远程调用");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return tid;
            }, executor);
            future.thenAccept(tid -> {
                System.out.println("线程" + tid + "结束了");
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
            //输出统计结果
            float time = System.currentTimeMillis() - start;

            System.out.println("所有任务已经执行完毕");
            System.out.println("运行的时长为(ms)：" + time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new CompletableFutureTest().testMultiCallBack();
    }

}
