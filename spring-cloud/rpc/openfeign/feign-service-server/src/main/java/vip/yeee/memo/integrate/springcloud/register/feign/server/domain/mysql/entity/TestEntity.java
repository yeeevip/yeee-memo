package vip.yeee.memo.integrate.springcloud.register.feign.server.domain.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/8/18 17:23
 */
@Data
@TableName("t_test_0")
public class TestEntity {

    private Long id;

    private String field1;

    private String field2;

}
