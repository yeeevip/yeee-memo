package vip.yeee.memo.integrate.redisson.test;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.redisson.kit.DelayQueueKit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/9/17 17:40
 */
@Slf4j
@Component
public class TestService {

    @Resource
    private DelayQueueKit delayQueueKit;

    private final static String QUEUE_CODE = "test_queue";

    @PostConstruct
    public void init() throws Exception {
        ThreadUtil.execAsync(() -> {
            try {
                this.gen();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        ThreadUtil.execAsync(this::get);
    }

    private void gen() throws InterruptedException {
        long id = 10000L;
        while (true) {
            delayQueueKit.addDelayQueue(id, 100, TimeUnit.SECONDS, QUEUE_CODE);
            TimeUnit.SECONDS.sleep(10);
//            id = id + 1;
        }
    }

    private void get() {
        while (true) {
            RBlockingDeque<Object> delayQueue = delayQueueKit.getDelayQueue(QUEUE_CODE);
            try {
                Long id = (Long) delayQueue.take();
                log.error("获取元素成功 - id = {}", id);
            } catch (InterruptedException e) {
                log.error("获取元素失败");
            }
        }
    }

}
