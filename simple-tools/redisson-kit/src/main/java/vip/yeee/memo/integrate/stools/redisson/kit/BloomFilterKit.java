package vip.yeee.memo.integrate.stools.redisson.kit;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/9/17 18:28
 */
@Slf4j
@Component
public class BloomFilterKit {

    @Resource
    private RedissonClient redissonClient;

    public void initBloomFilter(String name, List<String> elements) {
        log.info("初始化布隆过滤器：name = {}, elements = {}", name, elements);
        if (CollectionUtil.isEmpty(elements)) {
            return;
        }
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(name);
        bloomFilter.tryInit(elements.size(), 0.01);
        elements.forEach(bloomFilter::add);
        log.info("初始化布隆过滤器：完成！！！");
    }

    public boolean checkContainsEle(String name, String ele) {
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(name);
        if (bloomFilter == null || StrUtil.isBlank(ele)) {
            return false;
        }
        return bloomFilter.contains(ele);
    }

}
