package vip.yeee.memo.integrate.base.swagger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;

/**
 * description......
 *
 * @author yeeee
 * @since 2022/7/15 13:45
 */
@Slf4j
//@ConditionalOnProperty(value = "yeee.swagger.enable")
@ComponentScan({"vip.yeee.memo.integrate.base.swagger.config", "springfox.documentation.schema"})
public class SwaggerAutoConfigure {

    public SwaggerAutoConfigure() {
        log.info("自动配置-swagger");
    }
}
