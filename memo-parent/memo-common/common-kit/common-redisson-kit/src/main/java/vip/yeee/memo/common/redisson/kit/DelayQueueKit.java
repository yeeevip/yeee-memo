package vip.yeee.memo.common.redisson.kit;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import vip.yeee.memo.common.redisson.util.Md5Util;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/9/17 17:34
 * @see vip.yeee.memo.demo.stools.kit.service.DelayQueueService
 */
@Slf4j
@Component
public class DelayQueueKit {

    @Resource
    private RedissonClient redissonClient;

    public final static String QUEUE_PREFIX = "YEEEE:DELAY_QUEUE:";

    private final boolean isReSend= false;
    private final Long[] RESEND_DELAYED_SECONDS = {TimeUnit.SECONDS.toSeconds(15)
            , TimeUnit.MINUTES.toSeconds(1), TimeUnit.MINUTES.toSeconds(10)
            , TimeUnit.HOURS.toSeconds(1), TimeUnit.HOURS.toSeconds(5)};
    private final Map<String, AtomicInteger> MSG_RETRY_COUNTER = new ConcurrentHashMap<>();

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
        if (removeOld) {
            delayedQueue.remove(msg);
        }
        log.info("【队列-{}】- 发送消息：{} - DELAY：{}", queueCode, msg, delay);
        delayedQueue.offer(msg, delay, timeUnit);
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

    // while内不可【return或者break】，用【continue】，否则就直接中断了不会循环阻塞获取元素
    public <T> void consumeQueueMsg(String queueCode, Consumer<T> handler) {
        RBlockingDeque<T> blockingDeque = this.getDelayQueue(queueCode);
        RDelayedQueue<T> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        log.info("【队列-{}】- 监听队列成功", queueCode);
        while (true) {
            T ele = null;
            try {
                ele = blockingDeque.take();
                handler.accept(ele);
                log.info("【队列-{}】- 处理元素成功 - ele = {}", queueCode, ele);
            } catch (Exception e) {
                log.error("【队列-{}】- 处理元素失败 - ele = {}", queueCode, ele, e);
                if (isReSend) {
                    String md5 = Md5Util.md5(ele.toString());
                    AtomicInteger atomicInteger = Optional
                            .ofNullable(MSG_RETRY_COUNTER.get(md5))
                            .orElseGet(() -> {
                                AtomicInteger count = new AtomicInteger(0);
                                MSG_RETRY_COUNTER.put(md5, count);
                                return count;
                            });
                    int index = Math.min(atomicInteger.getAndIncrement(), RESEND_DELAYED_SECONDS.length - 1);
                    addDelayQueue(queueCode, ele, RESEND_DELAYED_SECONDS[index], TimeUnit.SECONDS);
                }
            }
        }
    }

}
