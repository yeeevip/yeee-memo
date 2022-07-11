package vip.yeee.memo.integrate.distribute.lock.jedis.lua.impl_juc_lock;

import vip.yeee.memo.integrate.distribute.lock.jedis.JedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * description ...
 *
 * @author yeeeee
 * @since 2021/12/22 19:25
 */
@Slf4j
public class JedisLockTest {

    // private static final Jedis jedis = JedisRepository.getJedis();

    private static int count = 0;

    private final ExecutorService pool = Executors.newFixedThreadPool(10);

    @Test
    public void testLock() {
        int threads = 100;
        CountDownLatch countDownLatch = new CountDownLatch(threads);
        long start = System.currentTimeMillis();
        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {

                String requestId = UUID.randomUUID().toString();

                Jedis jedis = JedisRepository.getJedis();

                Lock lock = new JedisLock(jedis, "test:lock:1", requestId);
                boolean locked = false;
                try {
                    locked = lock.tryLock(5, TimeUnit.SECONDS);

                    if (locked) {
                        for (int j = 0; j <  1000; j++) {
                            count++ ;
                        }
                        log.info("count = " + count);
                    } else {
                        log.info("抢锁失败");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (locked) lock.unlock();
                    jedis.close();
                }
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("10个线程每个累加1000为： = " + count);
        //输出统计结果
        float time = System.currentTimeMillis() - start;

        log.info("运行的时长为(ms)：" + time);
        log.info("每一次执行的时长为(ms)：" + time / count);
    }

}
