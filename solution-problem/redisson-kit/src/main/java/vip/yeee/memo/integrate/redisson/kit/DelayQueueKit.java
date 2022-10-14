package vip.yeee.memo.integrate.redisson.kit;

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
@Component
public class DelayQueueKit {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 添加延迟队列
     *
     * @param value     队列值
     * @param delay     延迟时间
     * @param timeUnit  时间单位
     * @param queueCode 队列键
     */
    public <T> void addDelayQueue(T value, long delay, TimeUnit timeUnit, String queueCode) {
        RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque(queueCode);
        RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        delayedQueue.offer(value, delay, timeUnit);
    }

    /**
     * 获取延迟队列
     */
    public RBlockingDeque<Object> getDelayQueue(String queueCode) {
        return redissonClient.getBlockingDeque(queueCode);
    }

}
