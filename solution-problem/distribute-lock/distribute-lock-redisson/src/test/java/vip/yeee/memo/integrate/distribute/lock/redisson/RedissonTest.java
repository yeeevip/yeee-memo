package vip.yeee.memo.integrate.distribute.lock.redisson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

/**
 * description ...
 *
 * @author yeeeee
 * @since 2021/12/23 17:03
 */
@Slf4j
@SpringBootTest
public class RedissonTest {

    @Autowired
    RedissonClient redissonClient;

    @Test
    public void testRBucketExample() {

        RBucket<String> rString = redissonClient.getBucket("redisson:test:bucket:string");
        rString.delete();
        rString.set("this is a string");

        RBucket<JSON> rObj = redissonClient.getBucket("redisson:test:bucket:obj");
        rObj.delete();
        JSONObject obj = new JSONObject();
        obj.put("aa", "aa");
        obj.put("bb", "bb");
        rObj.set(obj);

        log.info("string is ：" + rString.get());
        log.info("obj is ：" + rObj.get());

    }

    @Test
    public void testRListExample() {

        RList<String> nameList = redissonClient.getList("redisson:test:nameList");
        nameList.clear();
        nameList.add("张三");
        nameList.add("李四");
        nameList.add("王五");
        nameList.remove(-1);

        log.info("List size：" + nameList.size());
        log.info("Is list contains '李四' : " + nameList.contains("李四"));
        log.info("nameList = {}", nameList);

    }

    @Test
    public void testRMapExample() {

        RMap<String, Object> rMap = redissonClient.getMap("redisson:test:personalMap");
        rMap.clear();
        rMap.put("name", "张三");
        rMap.put("address", "北京");
        rMap.put("age", new Integer(10));

        log.info("Map size：" + rMap.size());
        log.info("Is map contains key 'age'：" + rMap.containsKey("age"));
        log.info("Value mapped by key 'name'：" + rMap.get("name"));

    }

    @Test
    public void testLuaExample() {

        redissonClient.getBucket("redisson:test:foo").set("bar");
        String value = redissonClient.getScript().eval(RScript.Mode.READ_ONLY, "return redis.call('get', 'redisson:test:foo')", RScript.ReturnType.VALUE);

        log.info("foo：" + value);

        // 通过预存的脚本进行同样的操作
        RScript rScript = redissonClient.getScript();
        // 首先将脚本加载到redis
        String sha1 = rScript.scriptLoad("return redis.call('get', 'redisson:test:foo')");
        // sha1 脚本ID
        log.info("sha1 = " + sha1);

        // 再通过SHA值调用脚本
        RFuture<Object> res = redissonClient.getScript().evalShaAsync(RScript.Mode.READ_ONLY, sha1, RScript.ReturnType.VALUE, Collections.emptyList());
        try {
            log.info("res = " + res.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    static int count = 0;
    @Test
    public void testRLockExample() {

        int threads = 100;
        ExecutorService pool = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(threads);

        long start = System.currentTimeMillis();
        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {
                RLock lock = redissonClient.getLock("redisson:test:lock；1");
                lock.lock();
                try {
                    for (int j = 0; j < 1000; j++) {
                        count++;
                    }
                } finally {
                    lock.unlock();
                }

                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("所有线程累加count = " + count);
        long runTime = System.currentTimeMillis() - start;
        log.info("运行的时长为：" + runTime);
        log.info("每一次执行的时长平均为：" + runTime / threads);

    }

    @Test
    public void testRAtomicLongExample() {

        RAtomicLong atomicLong = redissonClient.getAtomicLong("redisson:test:myAtomicLong");

        int threads = 100;
        ExecutorService pool = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {
                for (int j = 0; j < 1000; j++) {
                    atomicLong.getAndIncrement();
                }
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("atomicLong : " + atomicLong.get());

    }

    @Test
    public void testRLongAdderExample() {

        RLongAdder longAdder = redissonClient.getLongAdder("redisson:test:myLongAdder");

        int threads = 100;
        ExecutorService pool = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {
                for (int j = 0; j < 1000; j++) {
                    longAdder.increment();
                }
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("longAdder : " + longAdder.sum());

    }

    public static void main(String[] args) {
        System.out.println("temp11...");
        System.out.println("temp11...");
        System.out.println("temp11...");
    }

}
