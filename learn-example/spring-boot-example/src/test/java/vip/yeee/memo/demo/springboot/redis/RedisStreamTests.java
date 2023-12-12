package vip.yeee.memo.demo.springboot.redis;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import vip.yeee.memo.base.redis.utils.RedisUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/7/12 9:14
 */
@Slf4j
@SpringBootTest
public class RedisStreamTests {

    @Resource
    private RedisUtil redisUtil;

    String key = "TEST_STREAM_KEY";

    @Test
    public void test() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ThreadUtil.newThread(() -> {
            for (int i = 5; i < 4; i++) {
                RecordId recordId = redisUtil.addStream(key, "message-" + i);
                log.info("generated msg，recordId = {}", recordId);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "生产消息").start();
        ThreadUtil.newThread(() -> {
            String group = "group1";
            redisUtil.createGroup(key, group);
            while (true) {
                List<MapRecord<String, String, String>> recordList = redisUtil.readStream(key, "user-app", group);
                for (MapRecord<String, String, String> mapRecord : recordList) {
                    log.info("consume msg，message = {}", JSONUtil.toJsonPrettyStr(mapRecord));
                }
            }
        }, "消费消息1").start();
        ThreadUtil.newThread(() -> {
            String group = "group1";
            redisUtil.createGroup(key, group);
            while (true) {
                List<MapRecord<String, String, String>> recordList = redisUtil.readStream(key, "user-app", group);
                for (MapRecord<String, String, String> mapRecord : recordList) {
                    log.info("consume msg，message = {}", JSONUtil.toJsonPrettyStr(mapRecord));
                }
            }
        }, "消费消息2").start();
        countDownLatch.await();
    }

}
