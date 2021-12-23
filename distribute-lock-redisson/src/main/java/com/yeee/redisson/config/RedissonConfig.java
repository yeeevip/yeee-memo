package com.yeee.redisson.config;

import com.yeee.redisson.codec.FastjsonCodec;
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
 * @author yeeeee
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

}
