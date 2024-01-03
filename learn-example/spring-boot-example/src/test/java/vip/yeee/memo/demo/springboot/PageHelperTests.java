package vip.yeee.memo.demo.springboot;

import cn.hutool.core.thread.ThreadUtil;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import vip.yeee.memo.demo.springboot.domain.mysql.entity.SysUser;
import vip.yeee.memo.demo.springboot.domain.mysql.mapper.SysUserMapper;
import vip.yeee.memo.demo.springboot.domain.sqlserver.entity.SqlServerTestA;
import vip.yeee.memo.demo.springboot.domain.sqlserver.mapper.SqlServerTestAMapper;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author yeeee
 * @since 2024/1/2 14:44
 */
@Slf4j
@SpringBootTest
public class PageHelperTests {

    @Resource
    private SqlServerTestAMapper sqlServerTestAMapper;
    @Resource
    private SysUserMapper sysUserMapper;

    @Test
    public void test() throws InterruptedException {

        for (int i = 0; i < 20; i++) {
            ThreadUtil.execAsync(() -> {
                for (int j = 0; j < 10; j++) {
                    PageHelper.startPage(1, 10);
                    List<SysUser> sysUserList = sysUserMapper.selectAll();
                    log.info("sysUserList = {}", sysUserList.size());

                    PageHelper.startPage(1, 10);
                    List<SqlServerTestA> serverTestAList = sqlServerTestAMapper.selectAll();
                    log.info("serverTestAList = {}", serverTestAList.size());

                    PageHelper.startPage(1, 10);
                    sysUserList = sysUserMapper.selectAll();
                    log.info("sysUserList = {}", sysUserList.size());

                    PageHelper.startPage(2, 10);
                    serverTestAList = sqlServerTestAMapper.selectAll();
                    log.info("serverTestAList = {}", serverTestAList.size());

                    PageHelper.startPage(2, 10);
                    sysUserList = sysUserMapper.selectAll();
                    log.info("sysUserList = {}", sysUserList.size());
                }
            });
        }

        TimeUnit.SECONDS.sleep(200);

    }
}
