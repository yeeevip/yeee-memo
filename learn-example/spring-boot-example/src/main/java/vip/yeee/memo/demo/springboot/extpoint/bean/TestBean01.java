package vip.yeee.memo.demo.springboot.extpoint.bean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/8/14 9:51
 */
@Slf4j
@Component
@Data
public class TestBean01 {

    @Autowired
    private TestBean03 testBean03;

}
