package vip.yeee.memo.integrate.springcloud.register.feign.server.domain.mysql.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vip.yeee.memo.integrate.springcloud.register.feign.server.domain.mysql.entity.TestEntity;
import vip.yeee.memo.integrate.springcloud.register.feign.server.domain.mysql.mapper.TestEntityMapper;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/18 17:56
 */
@Service
public class TestEntityService extends ServiceImpl<TestEntityMapper, TestEntity> {
}
