package vip.yeee.memo.common.domain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2022/7/12 11:49
 */
@ComponentScan("vip.yeee.memo.common.domain")
@MapperScan("vip.yeee.memo.common.domain.mapper")
public class CommonServiceAutoConfigure {
}
