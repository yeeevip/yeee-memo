package vip.yeee.memo.demo.stools.kit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;
import vip.yeee.memo.demo.stools.kit.service.DelayQueueService;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/9/17 17:40
 */
@Slf4j
@SpringBootTest
public class RedissionDelayQueueTests implements ApplicationRunner {

    @Resource
    private DelayQueueService delayQueueService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(() -> delayQueueService.consumeTestQueueMsg(this::handle)).start();
    }

    @Test
    public void genRepeatMsg() throws InterruptedException {
        int id = 10000;
        for (int i = 0; i < 100; i++) {
            delayQueueService.addTestQueue(id, System.currentTimeMillis() + 5000);
        }
        new CountDownLatch(1).await();
    }

    @Test
    public void genMsg() throws InterruptedException {
        int id = 10000;
        for (int i = 0; i < 100; i++) {
            delayQueueService.addTestQueue(id + i, System.currentTimeMillis() + 5000);
        }
        new CountDownLatch(1).await();
    }

    // new Thread(this::get).start();
    private void handle(Integer ele) {
        log.info("【Redission延时队列】- 收到消息 - msg = {}", ele);
    }

}
