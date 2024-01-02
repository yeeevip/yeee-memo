package vip.yeee.memo.demo.springboot;

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
    public void test() {

        // pagehelper.auto-dialect=false 不管用

        PageHelper.startPage(1, 10).using("mysql");
        List<SysUser> sysUserList = sysUserMapper.selectAll();
        log.info("sysUserList = {}", sysUserList);

        PageHelper.startPage(1, 10).using("sqlserver");
        List<SqlServerTestA> serverTestAList = sqlServerTestAMapper.selectAll();
        log.info("serverTestAList = {}", serverTestAList);

    }
}
