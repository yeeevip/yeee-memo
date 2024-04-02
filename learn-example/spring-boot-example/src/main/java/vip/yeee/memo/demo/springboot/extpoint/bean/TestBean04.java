package vip.yeee.memo.demo.springboot.extpoint.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/8/14 9:51
 */
@Component
@Data
public class TestBean04 {

    private String field1;

    private String field2;

//    @Autowired
//    private TestBean03 testBean03;
}
