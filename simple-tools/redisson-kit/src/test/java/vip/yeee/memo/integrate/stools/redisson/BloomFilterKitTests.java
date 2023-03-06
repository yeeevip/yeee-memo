package vip.yeee.memo.integrate.stools.redisson;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.test.context.SpringBootTest;
import vip.yeee.memo.integrate.stools.redisson.RedissonKitApplication;
import vip.yeee.memo.integrate.stools.redisson.kit.BloomFilterKit;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/10/14 10:17
 */
@Slf4j
@SpringBootTest(classes = RedissonKitApplication.class)
public class BloomFilterKitTests implements InitializingBean {

    @Resource
    private BloomFilterKit bloomFilterKit;

    private final static List<String> elements = Lists.newArrayList();

    @Override
    public void afterPropertiesSet() throws Exception {
        for (int i = 0; i < 200; i++) {
            elements.add(UUID.randomUUID().toString());
        }
        this.testInitBloomFilter();
    }

    private void testInitBloomFilter() {
        String bloomName = "TEST_BLOOM_FILTER";
        List<String> existsElements = Lists.newArrayList();
        for (int i = 0; i < 200; i++) {
            if (i % 2 == 0) {
                existsElements.add(elements.get(i));
            }
        }
        bloomFilterKit.initBloomFilter(bloomName, existsElements);
    }

    @Test
    public void testCheckContainsEle() {
        String bloomName = "TEST_BLOOM_FILTER";
        int counter = 0;
        for (String element : elements) {
            if (bloomFilterKit.checkContainsEle(bloomName, element)) {
                counter++;
            }
        }
        log.info("存在的元素个数为：{}", counter);
    }

}
