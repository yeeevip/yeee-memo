package vip.yeee.memo.integrate.stools.kit.kit;

import cn.hutool.core.date.DateUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class CheckRepeatKit {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private boolean canRepeatOpr(String redisKey, long expire) {
        try {
            Boolean res = stringRedisTemplate
                    .opsForValue()
                    .setIfAbsent("YEEE:CAN_REPEAT_OPR:" + redisKey, "1", expire, TimeUnit.SECONDS);
            return Boolean.TRUE.equals(res);
        } catch (Exception e) {
            return true;
        }
    }

    public boolean canRepeatScheOpr(long s) {
        String key = "scheOpr:" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm");
        return this.canRepeatOpr(key, s);
    }

    public boolean canRepeatWishUpdate(Long wishId, int s) {
        String key = "wishUpdate:" + wishId;
        return this.canRepeatOpr(key, s);
    }
}
