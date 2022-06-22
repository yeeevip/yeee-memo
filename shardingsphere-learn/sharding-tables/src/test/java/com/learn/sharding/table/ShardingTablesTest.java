package com.learn.sharding.table;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.sharding.table.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * description......
 *
 * @author yeeee
 * @since 2022/6/22 17:22
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShardingTablesApplication.class)
public class ShardingTablesTest {

    @Autowired
    private TestService testService;

    @Test
    public void testGetList() {
        LambdaQueryWrapper<com.learn.sharding.table.entity.Test> wrapper = Wrappers.<com.learn.sharding.table.entity.Test>lambdaQuery()
                .orderByAsc(com.learn.sharding.table.entity.Test::getId);
        testService.page(new Page<>(1, 20), wrapper)
                .getRecords()
                .forEach(item -> log.info("id = {}", item.getId()));
    }

    @Test
    public void testInsertData() {
        for (int i = 0; i < 100000; i++) {
            com.learn.sharding.table.entity.Test data = new com.learn.sharding.table.entity.Test();
            data.setId(IdUtil.getSnowflake(1, 1).nextId());
            data.setType(i % 2 + 1);
            data.setField1(data.getId() + "");
            data.setField2(data.getId() + "");
            testService.save(data);
        }
    }

}
