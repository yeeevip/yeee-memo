package vip.yeee.memo.integrate.jdk.base.atomic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.LongAdder;

/**
 * description ...
 *
 * @author yeeeee
 * @since 2022/3/9 9:59
 */
@Slf4j
public class AtomicTest {

    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 4, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000));

    private static final Integer taskNum = 100;

    private static final AtomicInteger atomicInteger = new AtomicInteger(0);

    private static final LongAdder longAdder = new LongAdder();

    private static final AtomicReference<Integer> atomicRef = new AtomicReference<>(Integer.valueOf("0"));

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(taskNum);
        for (int i = 0; i < taskNum; i++) {
            int finalI = i;
            threadPoolExecutor.execute(() -> {

                // atomicInteger.incrementAndGet();
                // atomicRef.compareAndSet(Integer.valueOf(finalI), Integer.valueOf(finalI + 1));
                longAdder.add(1);

                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        log.info("result = {}", longAdder.longValue());

        threadPoolExecutor.shutdown();

    }

}


