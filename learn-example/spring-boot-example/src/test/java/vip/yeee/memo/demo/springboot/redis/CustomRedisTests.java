package vip.yeee.memo.demo.springboot.redis;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/7/12 9:14
 */
@Slf4j
@SpringBootTest
public class CustomRedisTests {

    @Resource
    private RedisTemplate<String, Object> customeRedisTemplate01;

    private DefaultRedisScript<CustomObject> updateObjectLuaScript = null;

    String oldKey = "YEEE:TEST:OLD";
    String newKey = "YEEE:TEST:NEW";

    @PostConstruct
    public void init() {
        CustomObject customObject = new CustomObject();
        customObject.setId("001");
        customObject.setStatus(1);
        customObject.setCreateTime(DateUtil.beginOfDay(DateUtil.yesterday()));
        customeRedisTemplate01.opsForValue().set(oldKey, customObject);

        updateObjectLuaScript = new DefaultRedisScript<>();
        updateObjectLuaScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/updateobject.lua")));
        updateObjectLuaScript.setResultType(CustomObject.class);

    }

    @Test
    public void testExecLuaScript() {
        CustomObject old = (CustomObject) customeRedisTemplate01.opsForValue().get(oldKey);
        log.info("OLD = {}", old);

        ArrayList<String> keys = Lists.newArrayList(oldKey, newKey);
        CustomObject replaced = customeRedisTemplate01.execute(updateObjectLuaScript, keys, "111111111", "用户1", new Date().getTime());
        log.info("REPLACED = {}", replaced);

        CustomObject news = (CustomObject) customeRedisTemplate01.opsForValue().get(newKey);
        log.info("NEW = {}", news);
    }

    @Data
    public static class CustomObject {

        private String id;

        private String exchangeUserId;

        private String exchangeUser;

        private String exchangeNo;

        private Date exchangeTime;

        private Integer status;

        private Date createTime;

    }

}
