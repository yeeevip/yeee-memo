package vip.yeee.memo.demo.jdk.base.threadPool;


import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * description ...
 *
 * @author yeeeee
 * @since 2021/12/28 18:12
 */
@Slf4j
public class CompletableFutureTest {

    /**
     * thenAccept、exceptionally方法的使用
     * @author yeeeeee
     * @since 2022/1/4 10:56
     */
    public void testThenAcceptAndExceptionally() throws InterruptedException {
        // 创建异步执行任务
        CompletableFuture<Double> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Math.random() < 0.3) {
                throw new RuntimeException("fetch price failed!");
            }
            return 5 + Math.random() * 20;
        });
        // 执行成功
        future.thenAccept(res -> log.info("result = " + res))
                // 执行失败
                .exceptionally(e -> {
                    log.info("execute err", e);
                    return null;
                });

        // 上面的方法异步，此处代码不一定会在future后面执行
        log.info("-------------- main mark ---------------");

        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭
        TimeUnit.MILLISECONDS.sleep(200);
    }

    /**
     * 使用thenApplyAsync实现多任务的串行异步执行
     * @author yeeeeee
     * @since 2022/1/4 11:33
     */
    public void testThenApplyAsync() throws InterruptedException {
        // 第一个任务
        CompletableFuture<String> queryFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "CODE_001";
        });
        // 第二个任务
        CompletableFuture<Double> fetchFuture = queryFuture.thenApplyAsync(code -> {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 5 + Math.random() * 20;
        });
        fetchFuture.thenAccept(res -> log.info("result = " + res));

        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭
        TimeUnit.MILLISECONDS.sleep(2000);
    }

    /**
     * anyOf并行处理执行任务，任意一个完成就会执行
     * @author yeeeeee
     * @since 2022/1/4 11:48
     */
    public void testAnyOf() throws InterruptedException {
        // 两个CompletableFuture执行异步查询
        CompletableFuture<String> cfQueryFromSina = CompletableFuture.supplyAsync(() -> {
            log.info("query code from https://finance.sina.com.cn/code/ ....");
            try {
                TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "602285001";
        });
        CompletableFuture<String> cfQueryFrom163 = CompletableFuture.supplyAsync(() -> {
            log.info("query code from https://money.163.com/code/ ....");
            try {
                TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "602285001";
        });

        // 用anyOf合并为一个新的CompletableFuture
        CompletableFuture<Object> cfQuery = CompletableFuture.anyOf(cfQueryFromSina, cfQueryFrom163);

        // 两个CompletableFuture执行异步查询
        CompletableFuture<Double> cfFetchFromSina  = cfQuery.thenApplyAsync(code -> {
            log.info("query price from https://finance.sina.com.cn/price/ ...");
            try {
                TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 5 + Math.random() * 20;
        });
        CompletableFuture<Double> cfFetchFrom163  = cfQuery.thenApplyAsync(code -> {
            log.info("query price from https://money.163.com/price/ ...");
            try {
                TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 5 + Math.random() * 20;
        });

        // 用anyOf合并为一个新的CompletableFuture
        CompletableFuture.anyOf(cfFetchFromSina, cfFetchFrom163).thenAccept(res -> {
            log.info("price : " + res);
        });

        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭
        TimeUnit.MILLISECONDS.sleep(200);

    }

    /**
     * allOf()，所有CompletableFuture执行完毕后回调
     * @author yeeeeee
     * @since 2022/1/4 13:40
     */
    public void testAllOf() throws InterruptedException {
        // 创建所有异步任务
        List<CompletableFuture<Double>> cfList = Stream.of("https://finance.sina.com.cn/price/", "https://money.163.com/price/")
                .map(url -> CompletableFuture.supplyAsync(() -> {
                    log.info("query price from {} ...", url);
                    try {
                        TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return 5 + Math.random() * 20;
                }))
                .collect(Collectors.toList());
        // 指定所有任务执行完毕后的异步回调
        CompletableFuture.allOf(cfList.toArray(new CompletableFuture[0])).thenApplyAsync(v -> {
            return cfList.stream().map(cf -> {
                try {
                    return cf.get().toString();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.joining("#"));
        }).thenAccept(res -> log.info("All task has executed completely!!!，res = " + res));

        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭
        TimeUnit.MILLISECONDS.sleep(200);
    }

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

    public static void main(String[] args) throws InterruptedException {
        new CompletableFutureTest().testAllOf();
    }

}
