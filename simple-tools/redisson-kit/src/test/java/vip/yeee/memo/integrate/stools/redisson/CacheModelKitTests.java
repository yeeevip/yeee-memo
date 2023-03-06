package vip.yeee.memo.integrate.stools.redisson;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RSet;
import org.springframework.boot.test.context.SpringBootTest;
import vip.yeee.memo.integrate.stools.redisson.kit.CacheModelKit;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/12/19 15:41
 */
@Slf4j
@SpringBootTest
public class CacheModelKitTests {

    @Resource
    private CacheModelKit cacheModelKit;

    @Test
    public void testCacheObject() {
        String key = "yeee:test:object";
        cacheModelKit.cacheObject(key, new CacheModel(1, "dd"), 100);

        CacheModel object = (CacheModel)cacheModelKit.getObject(key);
        log.info("object = {}", object);
    }

    @Test
    public void testCacheList() {
        String key = "yeee:test:list";
        cacheModelKit.cacheList(key, Collections.singletonList(new CacheModel(1, "dd")), 100);

        RList<?> rList = cacheModelKit.getList(key);
        log.info("all = {}", rList.readAll());
        log.info("range = {}", rList.range(0, 1));
    }

    @Test
    public void testCacheSet() {
        String key = "yeee:test:set";
        List<CacheModel> adds = Lists.newArrayList();
        for (int i = 0; i < 20; i++) {
            adds.add(new CacheModel(i, "d" + i + "d"));
        }
        cacheModelKit.cacheSet(key, adds, 100);

        RSet<?> rSet = cacheModelKit.getSet(key);
        log.info("all = {}", rSet.readAll());
        log.info("random = {}", rSet.removeRandom());
    }

    @Test
    public void testCacheZSet() {
        String key = "yeee:test:zset";
        Map<Object, Double> adds = Maps.newHashMap();
        for (int i = 0; i < 20; i++) {
            adds.put("第" + i + "个", (double) i);
        }
        cacheModelKit.cacheZSet(key, adds, 100);

        RScoredSortedSet<Object> zSet = cacheModelKit.getZSet(key);
        log.info("all = {}", zSet.readAll());
        log.info("addScore = {}", zSet.addScore("第0个", 100));
        log.info("addScoreAndGetRank = {}", zSet.addScoreAndGetRank("第1个", 150));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CacheModel {
        private Integer id;
        private String name;
    }

}
