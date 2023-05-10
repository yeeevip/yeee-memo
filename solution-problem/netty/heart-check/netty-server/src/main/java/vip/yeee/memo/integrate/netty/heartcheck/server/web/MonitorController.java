package vip.yeee.memo.integrate.netty.heartcheck.server.web;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class MonitorController {

    private final String pattern = "heart:monitor:*";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @ResponseBody
    @RequestMapping("/getData.do")
    public Map<String,Object> getData() {
        Map<String,Object> map = new HashMap<>();
        Set<String> keys = redisTemplate.keys(pattern);
        if (CollectionUtil.isEmpty(keys)) {
            return map;
        }
        for (String k : keys) {
            List<String> pingList = redisTemplate.opsForList().range(k, 0, 9);
            if (CollectionUtil.isNotEmpty(pingList)) {
                Collections.reverse(pingList);
                int index = k.lastIndexOf(":");
                map.put(k.substring(index + 1), pingList);
            }
        }
        return map;
    }

    @RequestMapping("monitor.do")
    public String monitor() {
        return "monitor";
    }
}
