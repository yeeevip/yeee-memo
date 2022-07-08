package vip.yeee.memo.integrate.cache.jetcache;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/6/22 18:24
 */
@EnableMethodCache(basePackages = "vip.yeee.memo.integrate.cache.jetcache.cache")
@EnableCreateCacheAnnotation
@SpringBootApplication
public class JetcacheToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(JetcacheToolApplication.class, args);
    }

}
