package vip.yeee.memo.integrate.stools.redisson.test;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.springframework.stereotype.Component;
import vip.yeee.memo.integrate.stools.redisson.kit.DelayQueueKit;

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
//        ThreadUtil.execAsync(() -> {
//            try {
//                this.gen();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        ThreadUtil.execAsync(this::get);
        new Thread(this::get).start();
    }

    private void gen() throws InterruptedException {
        long id = 10000L;
        while (true) {
            delayQueueKit.addDelayQueue(QUEUE_CODE, id, 100, TimeUnit.SECONDS);
            TimeUnit.SECONDS.sleep(10);
//            id = id + 1;
        }
    }

    // new Thread(this::get).start();
    // while内不可【return或者break】，用【continue】，否则就直接中断了不会循环阻塞获取元素
    private void get() {
        while (true) {
            RBlockingDeque<Object> delayQueue = delayQueueKit.getDelayQueue(QUEUE_CODE);
            try {
                Long ele = (Long) delayQueue.take();
                // handle
                log.info("【队列-TEST】- 处理元素成功 - ele = {}", ele);
            } catch (Exception e) {
                log.error("【队列-TEST】- 处理元素失败", e);
            }
        }
    }

}
