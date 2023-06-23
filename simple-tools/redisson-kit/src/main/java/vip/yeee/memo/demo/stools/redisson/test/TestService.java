package vip.yeee.memo.demo.stools.redisson.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import vip.yeee.memo.demo.stools.redisson.kit.DelayQueueKit;

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
public class TestService implements ApplicationRunner {

    @Resource
    private DelayQueueKit delayQueueKit;

    private final static String QUEUE_CODE = "test_queue";

/*    @PostConstruct
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
    }*/

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        ThreadUtil.execAsync(() -> {
//            try {
//                this.gen();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        ThreadUtil.execAsync(this::get);
        new Thread(() -> delayQueueKit.consumeTestQueueMsg(this::handle)).start();
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
    private void handle(Integer ele) {

    }

}
