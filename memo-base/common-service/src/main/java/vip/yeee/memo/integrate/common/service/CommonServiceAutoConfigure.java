package vip.yeee.memo.integrate.common.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/7/12 11:49
 */
@ComponentScan("vip.yeee.memo.integrate.common.service")
@MapperScan("vip.yeee.memo.integrate.common.service.mapper")
public class CommonServiceAutoConfigure {
}
