package vip.yeee.memo.demo.springboot.domain.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Table;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2024/2/22 17:27
 */
@AllArgsConstructor
@Data
@Table(name = "t_temp_test1")
public class TempTest1 {

    private String id;
    private Integer count;
    private String parentId;
}
