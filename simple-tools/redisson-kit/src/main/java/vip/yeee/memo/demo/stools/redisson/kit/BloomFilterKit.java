package vip.yeee.memo.demo.stools.redisson.kit;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 判断【不存在则一定不存在】，判断存在则可能误判(hash冲突)
 *
 * @author yeeee
 * @since 2022/9/17 18:28
 */
@Slf4j
@Component
public class BloomFilterKit {

    @Resource
    private RedissonClient redissonClient;

    public void initOrAddEle(String name, List<String> elements) {
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(name);
        if (!bloomFilter.isExists()) {
            log.info("【布隆过滤器】- 初始化 - name = {}", name);
            bloomFilter.tryInit(elements.size(), 0.01);
//        bloomFilter.expire(expired, TimeUnit.MINUTES);
        }
        if (CollectionUtil.isNotEmpty(elements)) {
            log.info("【布隆过滤器】- 添加元素 - elementsSize = {}", elements.size());
            elements.forEach(bloomFilter::add);
        }
    }

    public boolean checkContainsEle(String name, String ele) {
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(name);
        if (bloomFilter == null || StrUtil.isBlank(ele)) {
            return false;
        }
        return bloomFilter.contains(ele);
    }

}
