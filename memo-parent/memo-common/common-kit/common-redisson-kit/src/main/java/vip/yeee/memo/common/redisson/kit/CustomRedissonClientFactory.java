package vip.yeee.memo.common.redisson.kit;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/12/14 9:47
 */
public class CustomRedissonClientFactory {

    private static final Map<String, RedissonClient> REDISSON_CLIENT_INSTANCE = new HashMap<>();

    public static RedissonClient getRedissonClientDb0(String host, String pwd, Integer db) {
        String key = host + "#" + db;
        if (REDISSON_CLIENT_INSTANCE.get(key) != null) {
            return REDISSON_CLIENT_INSTANCE.get(key);
        }
        synchronized (CustomRedissonClientFactory.class) {
            if (REDISSON_CLIENT_INSTANCE.get(key) != null) {
                return REDISSON_CLIENT_INSTANCE.get(key);
            }
            String redisAddress = host;
            String redisPwd = pwd;
            Config config = new Config();
            config.useSingleServer()
                    .setAddress("redis://" + redisAddress +":6379")
                    .setPassword(redisPwd)
                    .setDatabase(db);
            RedissonClient redissonClient = Redisson.create(config);
            REDISSON_CLIENT_INSTANCE.put(key, redissonClient);
            return redissonClient;
        }
    }
}
