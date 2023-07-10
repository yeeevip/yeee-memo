package vip.yeee.memo.common.redisson.kit;

import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/19 17:52
 */
@Component
public class RateLimiterKit {

    @Resource
    private RedissonClient redissonClient;

    public RRateLimiter rateLimiter(String limitName) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(limitName);
        rateLimiter.setRate(RateType.OVERALL, 3, 5, RateIntervalUnit.SECONDS);
        return rateLimiter;
    }

}
