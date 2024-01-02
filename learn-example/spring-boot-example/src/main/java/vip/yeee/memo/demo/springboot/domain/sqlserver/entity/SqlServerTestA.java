package vip.yeee.memo.demo.springboot.domain.sqlserver.entity;

import lombok.Data;

import javax.persistence.Table;

/**
 * description......
 *
 * @author yeeee
 * @since 2024/1/2 14:37
 */
@Data
@Table(name = "t_test_a")
public class SqlServerTestA {

    private String id;
    private String name;
    private String value;
}
