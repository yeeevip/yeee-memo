package vip.yeee.memo.demo.jdk.base.threadPool;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * description ...
 *
 * @author https://www.yeee.vipe
 * @since 2022/1/7 17:54
 */
@Slf4j
public class ThreadPoolTest {

    /**
     * 优点：创建一个固定大小线程池，超出的线程会在队列中等待。
     * 缺点：不支持自定义拒绝策略，大小固定，难以扩展。
     * @author https://www.yeee.vipe
    **/
    public void testExecutorsNewFixedThreadPool() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        List<Future<String>> futures = new ArrayList<>(4);
        for (int i = 1; i <= 4; i++) {
            Future<String> future = executorService.submit(() -> {
                log.info("线程 {} -> success", Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(1);
                return "success";
            });
            futures.add(future);
        }
        executorService.shutdown();

        log.info("main start");

        futures.forEach(future -> {
            try {
                String res = future.get(60, TimeUnit.SECONDS);
                log.info("result = {}", res);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 核心线程数是 0， 最大线程数是 Integer.MAX_VALUE，全部都是空闲线程60s后回收
     * @author https://www.yeee.vipe
    **/
    public void testExecutorsNewCachedThreadPool() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        ExecutorService executorService = Executors.newCachedThreadPool(r -> new Thread(r, "t" + atomicInteger.incrementAndGet()));

        log.info("main start");

        for (int i = 1; i <= 3; i++) {
            executorService.execute(() -> {
                log.info("execute task");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        log.info("main end");

    }

    /**
     * 优点：创建一个单线程的线程池，保证线程的顺序执行
     * 缺点：不适合并发。
     * @author https://www.yeee.vipe
     **/
    public void testExecutorsNewSingleThreadExecutor() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        log.info("main start");
        for (int i = 1; i <= 3; i++) {
            if (i == 2) {
                executorService.execute(() -> {
                    log.info("===异常task===");
                    int r = 10 / 0;
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                executorService.execute(() -> {
                    log.info("===task===");
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        executorService.shutdown();
        log.info("main end");
    }

    /**
     * 优点：创建一个固定大小线程池，可以定时或周期性的执行任务。
     * 缺点：一旦一个任务执行失败就不会再次执行
     * @author https://www.yeee.vipe
     **/
    public void testExecutorsNewScheduledThreadPool() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        log.info("main start");
        for (int i = 1; i <= 3; i++) {
            if (i == 2) {
                executorService.scheduleWithFixedDelay(() -> {
                    log.info("===异常task===");
                    int r = 10 / 0;
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }, 2 ,1, TimeUnit.SECONDS);
            } else {
                executorService.scheduleWithFixedDelay(() -> {
                    log.info("===task===");
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }, 2, 1, TimeUnit.SECONDS);
            }
        }
        //executorService.shutdown();
        log.info("main end");
    }

    /**
     * 任务提交的方式
     * void execute(Runnable command); 提交一个线程任务，没有返回值。
     * Future<?> submit；提交一个线程任务，有返回值。
     * List<Future<T>> invokeAll；提交所有的任务
     * T invokeAny；提交 tasks 中所有任务，哪个任务先成功执行完毕，返回此任务执行结果，其它任务取消
    **/
    public void testSubmitTaskByOtherWay() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        List<Future<String>> futures = executorService.invokeAll(Arrays.asList(
                () -> {
                    log.info("1");
                    return "1";
                },
                () -> {
                    log.info("2");
                    return "2";
                },
                () -> {
                    log.info("3");
                    return "3";
                },
                () -> {
                    log.info("4");
                    return "4";
                }
        ));

        String f = executorService.invokeAny(Arrays.asList(
                () -> {
                    log.info("1-");
                    TimeUnit.MILLISECONDS.sleep(100);
                    return "1-";
                },
                () -> {
                    log.info("2-");
                    TimeUnit.MILLISECONDS.sleep(200);
                    return "2-";
                },
                () -> {
                    log.info("3-");
                    TimeUnit.MILLISECONDS.sleep(50);
                    return "3-";
                },
                () -> {
                    log.info("4-");
                    TimeUnit.MILLISECONDS.sleep(200);
                    return "4-";
                }
        ));
        log.info("main start");
        futures.forEach(future -> {
            try {
                log.info(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        log.info("f = " + f);
        executorService.shutdown();
        log.info("main end");
    }

    /**
     * ThreadPoolExecutor 类创建线程池
    **/
    public void testNewThreadPoolExecutor() {
        // 核心
        int corePoolSize = 5;
        // 最大
        int maximumPoolSize = 10;
        // 超出corePoolSize小于等于max的线程的存活时间
        long keepAliveTime = 100;
        // 存活时间单位
        TimeUnit unit = TimeUnit.SECONDS;
        // 缓存队列 FIFO
        ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(5);
        // 设置工厂
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        // 设置线程饱和拒绝策略
        RejectedExecutionHandler policy = new ThreadPoolExecutor.AbortPolicy();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, policy);

        for (int i = 1; i <= 15; i++) {
            threadPoolExecutor.execute(new ThreadTask(i));
            log.info("线程中线程数目：{}，队列中等待执行的任务数量：{}，已执行完毕的任务数：{}", threadPoolExecutor.getPoolSize(), threadPoolExecutor.getQueue().size(), threadPoolExecutor.getCompletedTaskCount());
        }

    }

    static class ThreadTask implements Runnable {

        private final int num;

        public ThreadTask(int num) {
            this.num = num;
        }

        @SneakyThrows
        @Override
        public void run() {
            log.info("{} === 正在执行 task{}, time = {}", Thread.currentThread().getName(), num, new Date());
            TimeUnit.SECONDS.sleep(2);
            log.info("{} === 执行完毕 task{}, time = {}", Thread.currentThread().getName(), num, new Date());
        }
    }

    /**
     * 通过研究源码我们发现，ThreadPoolExecutor中阻塞队列添加任务时采用的是offer方法
     * LinkedBlockingQueue、ArrayBlockingQueue以及其他阻塞队列，offer方法在添加元素的时候，如果队列已满无法添加元素的时候，offer方法会直接返回false，这就会导致RejectedExecutionHandler的异常。
     * 我们自定义没重写 offer方法。
    **/
    public void testCustomizeBlockingQueue() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 200; i++) {
            list.add(i);
        }
        // workQueue满时用重写的offer调用put阻塞，而不是返回false，执行拒绝策略
        CustomizeBlockingQueue<Runnable> workQueue = new CustomizeBlockingQueue<>(50);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(25, 50, 3, TimeUnit.SECONDS, workQueue);
        for (Integer integer : list) {
            threadPoolExecutor.execute(() -> {
                log.info("线程名：{}，执行id={}", Thread.currentThread().getName(), integer);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        if (!threadPoolExecutor.isShutdown()) {
            threadPoolExecutor.shutdown();
            log.info("threadPoolExecutor 线程池关闭");
        }
    }

    static class CustomizeBlockingQueue<E> extends LinkedBlockingDeque<E> {
        public CustomizeBlockingQueue(int capacity) {
            super(capacity);
        }

        @Override
        public boolean offer(E e) {
            try {
                put(e);
                return true;
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            return false;
        }
    }

    /**
     * ScheduledThreadPoolExecutor实现了ScheduledExecutorService接口，该接口定义了可延时执行异步任务和可周期执行异步任务的特有功能。
    **/
    public void testScheduledThreadPoolExecutor() {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(5);
        // 延迟3s，然后每隔2s启动个线程去执行
        //scheduledThreadPoolExecutor.scheduleAtFixedRate(new ThreadTask(1), 3, 2, TimeUnit.SECONDS);
        // 延迟3s，任务执行完毕后2s后再继续这样执行后面的
        scheduledThreadPoolExecutor.scheduleWithFixedDelay(new ThreadTask(1), 3, 2, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        new ThreadPoolTest().testScheduledThreadPoolExecutor();
    }

}
