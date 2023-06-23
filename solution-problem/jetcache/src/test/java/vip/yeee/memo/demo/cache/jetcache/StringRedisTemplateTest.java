package vip.yeee.memo.demo.cache.jetcache;

import com.google.common.collect.Lists;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.*;

@SpringBootTest
public class StringRedisTemplateTest {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testHash() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "aaaa");
        map.put("aa", 2 + "");
        stringRedisTemplate.opsForHash().putAll("test:你好", map);
//        stringRedisTemplate.opsForHash().increment("test:1111", "num", 1);
//
//        stringRedisTemplate.opsForHash().put("test:11115", "cc", "2");
        List<String> objects = Lists.newArrayList();
        objects.add("aaaa");
        objects.add("aaab");
        objects.add("aaac");
        List<String> strings = stringRedisTemplate.opsForValue().multiGet(objects);
        System.out.println(strings);

    }

    @Data
    public static class AA {
        private String name;
        private Integer num;

    }

}
