package com.learn.sharding.table.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.sharding.table.entity.Test;
import com.learn.sharding.table.mapper.TestMapper;
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
