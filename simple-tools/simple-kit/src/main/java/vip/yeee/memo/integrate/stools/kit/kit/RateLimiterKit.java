package vip.yeee.memo.integrate.stools.kit.kit;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/1/31 17:27
 */
@Slf4j
public class RateLimiterKit {


    /**
     * Semaphore限制了【并发访问的数量】而不是使用速率
     */
    public void jdkSemaphore() {
        Semaphore semaphore = new Semaphore(10);
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> {
//                rateLimiter.acquire();
                boolean acquire = false;
                try {
                    acquire = semaphore.tryAcquire(1, 1, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!acquire) {
                    log.info("limiter-limit\n");
                }
                if(!acquire) {
                    log.info("limiter-limit\n");
                }
                String dateTime = new SimpleDateFormat("HH:mm:ss:SSS").format(new Date());
                log.info("limiter-{}\n", dateTime);
            });
            executorService.shutdown();
        }
    }

    /**
     *
     */
    public void googleGuavaRateLimiter() {
        int permitsPerSecond = 10; // 以每秒10个的速率
        final RateLimiter rateLimiter = RateLimiter.create(permitsPerSecond);
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> {
//                rateLimiter.acquire();
                boolean acquire = rateLimiter.tryAcquire(1, 1, TimeUnit.SECONDS);
                if(!acquire) {
                    log.info("limiter-limit\n");
                }
                String dateTime = new SimpleDateFormat("HH:mm:ss:SSS").format(new Date());
                log.info("limiter-{}\n", dateTime);
            });
        }
        executorService.shutdown();
    }

    public static void main(String[] args) {
        RateLimiterKit limiterKit = new RateLimiterKit();
//        limiterKit.googleGuavaRateLimiter();
        limiterKit.jdkSemaphore();
    }


}
