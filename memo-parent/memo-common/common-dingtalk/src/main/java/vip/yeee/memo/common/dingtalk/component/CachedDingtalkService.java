package vip.yeee.memo.common.dingtalk.component;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/6/20 12:02
 */
@Slf4j
@Component("cachedDingtalkService")
@Conditional(CachedDingtalkService.RedissonConditional.class)
public class CachedDingtalkService extends DingtalkService implements ApplicationRunner {

    @Resource
    private RedissonClient redissonClient;

    private final static String QUEUE_PREFIX = "YEEEE:DELAY_QUEUE:DINGTALK:";

    @Override
    public void sendChatGroupMsg(String group, String msg) {
        if (!StringUtils.hasText(group) || !StringUtils.hasText(chatGroup().get(group)) || !StringUtils.hasText(msg)) {
            log.warn("【钉钉群机器人】参数缺失！");
        }
        RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque(QUEUE_PREFIX + group);
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        delayedQueue.offer(msg, 1, TimeUnit.MILLISECONDS);
    }

    public void consumeQueueMsg(String group) {
        RBlockingDeque<String> delayQueue = redissonClient.getBlockingDeque(QUEUE_PREFIX + group);
        log.info("【队列-{}】- 监听队列成功", group);
        while (true) {
            List<String> ele = null;
            try {
                TimeUnit.MILLISECONDS.sleep(3100);
                ele = delayQueue.poll(20);
                if (ele.isEmpty()) {
                    continue;
                }
                super.sendChatGroupMsg(group, String.join("\n\n", ele));
                log.info("【队列-{}】- 处理元素成功 - ele = {}", delayQueue.getName(), ele.size());
            } catch (Exception e) {
                log.error("【队列-{}】- 处理元素失败 - ele = {}", delayQueue.getName(), ele != null ? ele.size() : null, e);
            }
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        chatGroup().forEach((g, a) -> new Thread(() -> this.consumeQueueMsg(g)).start());
    }

    public static class RedissonConditional implements Condition {
        @Override
        public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
            try {
                Class.forName("org.redisson.api.RedissonClient");
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        }
    }

}
