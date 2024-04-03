package vip.yeee.memo.demo.distribute.lock.redisson.config;

import vip.yeee.memo.demo.distribute.lock.redisson.codec.FastjsonCodec;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description ...
 *
 * @author https://www.yeee.vip
 * @since 2021/12/23 16:40
 */
@Configuration
public class RedissonConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String redisUrl = String.format("redis://%s:%s", redisProperties.getHost(), redisProperties.getPort());
        config.useSingleServer().setAddress(redisUrl).setPassword(redisProperties.getPassword());
        config.useSingleServer().setDatabase(redisProperties.getDatabase());
        config.setCodec(new FastjsonCodec());
        return Redisson.create(config);
    }

    @Bean
    public RedissonClient redissonClient1() {
        Config config1 = new Config();
        config1.useSingleServer().setAddress("redis://172.0.0.1:5378").setPassword("a123456").setDatabase(0);
        return Redisson.create(config1);
    }

    @Bean
    public RedissonClient redissonClient2() {
        Config config1 = new Config();
        config1.useSingleServer().setAddress("redis://172.0.0.1:5379").setPassword("a123456").setDatabase(0);
        return Redisson.create(config1);
    }

    @Bean
    public RedissonClient redissonClient3() {
        Config config1 = new Config();
        config1.useSingleServer().setAddress("redis://172.0.0.1:5380").setPassword("a123456").setDatabase(0);
        return Redisson.create(config1);
    }

}
