package vip.yeee.memo.demo.springboot.redis;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import vip.yeee.memo.base.redis.utils.RedisUtil;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/7/12 9:14
 */
@Slf4j
@SpringBootTest
public class RedisGeoTests {

    @Resource
    private RedisUtil redisUtil;

    String key = "TEST_GEO_KEY";

    @PostConstruct
    public void init() {
        redisUtil.addGeoLocation(key, 120.22867, 30.19345, "杭州");
        redisUtil.addGeoLocation(key, 120.59002 , 30.05766, "绍兴");
        redisUtil.addGeoLocation(key, 121.63084 , 29.86607, "宁波");
        redisUtil.addGeoLocation(key, 120.09319 , 30.91571, "湖州");
        redisUtil.addGeoLocation(key, 120.75231 , 30.75511, "嘉兴");
        redisUtil.addGeoLocation(key, 119.63218 , 29.08518, "金华");
        redisUtil.addGeoLocation(key, 121.49379 , 31.24576, "上海");
    }

    @Test
    public void test() {
        Point hz = redisUtil.getGeoPosition(key, "杭州");
        log.info("杭州的坐标：{}", hz);
        Distance distance = redisUtil.getGeoDistance(key, "杭州", "上海");
        log.info("杭州-上海的距离：{}", distance);
//        GeoResults<RedisGeoCommands.GeoLocation<String>> circleResults = redisUtil
//                .getGeoByCircle(key, "杭州", new Distance(100, Metrics.KILOMETERS));
        GeoResults<RedisGeoCommands.GeoLocation<String>> circleResults = redisUtil
                .getGeoRadius(key, "杭州", new Distance(500, Metrics.KILOMETERS));
        log.info("杭州100km的坐标：{}", JSONUtil.toJsonPrettyStr(circleResults));
    }

}
