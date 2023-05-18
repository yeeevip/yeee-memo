package vip.yeee.memo.integrate.stools.redisson.kit;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/9/17 17:34
 */
@Slf4j
@Component
public class DelayQueueKit {

    @Resource
    private RedissonClient redissonClient;

    private final static String QUEUE_PREFIX = "YEEEE:DELAY_QUEUE:";
    private final static String TEST_QUEUE = QUEUE_PREFIX + "TEST";

    /**
     * 添加延迟队列
     *
     * @param msg     队列值
     * @param delay     延迟时间
     * @param timeUnit  时间单位
     * @param queueCode 队列键
     */
    public <T> void addDelayQueue(String queueCode, T msg, boolean removeOld, long delay, TimeUnit timeUnit) {
        RBlockingDeque<T> blockingDeque = redissonClient.getBlockingDeque(queueCode);
        RDelayedQueue<T> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        delayedQueue.offer(msg, delay, timeUnit);
        if (removeOld) {
            delayedQueue.remove(msg);
        }
    }
    public <T> void addDelayQueue(String queueCode, T msg, long delay, TimeUnit timeUnit) {
        addDelayQueue(queueCode, msg, false, delay, timeUnit);
    }

    /**
     * 获取延迟队列
     */
    public <T> RBlockingDeque<T> getDelayQueue(String queueCode) {
        return redissonClient.getBlockingDeque(queueCode);
    }

    public RBlockingDeque<Integer> getTestQueue() {
        return this.getDelayQueue(DelayQueueKit.TEST_QUEUE);
    }

    public void addTestQueue(Integer ele, long delayTime) {
        log.info("【队列-TEST】- 添加元素，ele = {}", ele);
        this.addDelayQueue(DelayQueueKit.TEST_QUEUE, ele, Math.max(delayTime - System.currentTimeMillis(), 0), TimeUnit.MILLISECONDS);
    }


}
