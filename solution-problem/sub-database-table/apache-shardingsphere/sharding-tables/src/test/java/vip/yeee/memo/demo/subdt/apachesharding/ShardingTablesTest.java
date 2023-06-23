package vip.yeee.memo.demo.subdt.apachesharding;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import vip.yeee.memo.demo.subdt.apachesharding.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * description......
 *
 * @author yeeee
 * @since 2022/6/22 17:22
 */
@Slf4j
@SpringBootTest(classes = ShardingTablesApplication.class)
public class ShardingTablesTest {

    @Autowired
    private TestService testService;

    @Test
    public void testGetList() {
        LambdaQueryWrapper<vip.yeee.memo.demo.subdt.apachesharding.entity.Test> wrapper = Wrappers.<vip.yeee.memo.demo.subdt.apachesharding.entity.Test>lambdaQuery()
                .orderByAsc(vip.yeee.memo.demo.subdt.apachesharding.entity.Test::getId);
        testService.page(new Page<>(1, 20), wrapper)
                .getRecords()
                .forEach(item -> log.info("id = {}", item.getId()));
    }

    @Test
    public void testInsertData() {
        for (int i = 0; i < 100000; i++) {
            vip.yeee.memo.demo.subdt.apachesharding.entity.Test data = new vip.yeee.memo.demo.subdt.apachesharding.entity.Test();
            data.setId(IdUtil.getSnowflake(1, 1).nextId());
            data.setType(i % 2 + 1);
            data.setField1(data.getId() + "");
            data.setField2(data.getId() + "");
            testService.save(data);
        }
    }

}
