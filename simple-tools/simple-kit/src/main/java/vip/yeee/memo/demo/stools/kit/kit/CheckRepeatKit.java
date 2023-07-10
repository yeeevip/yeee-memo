package vip.yeee.memo.demo.stools.kit.kit;

import cn.hutool.core.date.DateUtil;
import org.springframework.stereotype.Component;
import vip.yeee.memo.base.redis.kit.RedisKit;

import javax.annotation.Resource;
import java.util.Date;

/**
 * description......
 * @author yeeee
 */
@Component
public class CheckRepeatKit {

    @Resource
    private RedisKit redisKit;

    public boolean canRepeatScheOpr(long s) {
        String key = "scheOpr:" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm");
        return redisKit.canRepeatOpr(key, s);
    }

    public boolean canRepeatOrderUpdate(Long wishId, int s) {
        String key = "orderUpdate:" + wishId;
        return redisKit.canRepeatOpr(key, s);
    }
}
