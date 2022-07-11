package vip.yeee.memo.integrate.subdt.apachesharding.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.yeee.memo.integrate.subdt.apachesharding.entity.Test;
import vip.yeee.memo.integrate.subdt.apachesharding.mapper.TestMapper;
import org.springframework.stereotype.Service;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/6/22 17:46
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {
}
