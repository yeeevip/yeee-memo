package vip.yeee.memo.demo.cache.jetcache.controller;

import vip.yeee.memo.demo.cache.jetcache.cache.CacheBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/6/22 18:26
 */
@RestController
public class TestJetcacheController {

    @Autowired
    private CacheBizService cacheBizService;

    @GetMapping("jetcache/test")
    public Object getData() {
        return cacheBizService.getData(null, null);
    }

}
