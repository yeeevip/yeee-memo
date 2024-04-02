package vip.yeee.memo.demo.subdt.apachesharding.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.yeee.memo.demo.subdt.apachesharding.entity.Test;
import vip.yeee.memo.demo.subdt.apachesharding.mapper.TestMapper;
import org.springframework.stereotype.Service;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/6/22 17:46
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements TestService {
}
