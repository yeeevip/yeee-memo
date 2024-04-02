package vip.yeee.memo.demo.springboot.extpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

/**
 * 扩展点01：ApplicationContextInitializer
 *
 * 1. 执行时机
 *
 * Spring容器初始化前（BeanDefinition生成前），springboot中refreshContext()前的prepareContext
 * SpringApplication.run -> prepareContext -> applyInitializers(context)
 *
 * 2. 应用
 *
 * 可以认为ApplicationContextInitializer实际上是Spring容器初始化前ConfigurableApplicationContext的回调接口，
 * 可以对上下文环境作一些操作，如运行环境属性注册、激活配置文件等
 *
 * 3. 如何注册到spring容器？
 *
 * 3.1 spring.factories
 *  在resources目录新建/META-INFI/spring.factories文件，
 *  org.springframework.context.ApplicationContextInitializer=vip.yeee.memo.demo.springboot.extpoint.Ext01ApplicationContextInitializer
 *
 * 3.2 application.properties
 *  context.initializer.classes=vip.yeee.memo.demo.springboot.extpoint.Ext01ApplicationContextInitializer
 *
 * 3.3 springApplication.addInitializers()
 *  SpringApplication springApplication = new SpringApplication(SpringbootExampleApplication.class);
 *  springApplication.addInitializers(new Ext01ApplicationContextInitializer());
 *
 * @author https://www.yeee.vip
 * @since 2023/8/14 9:28        -
 */
@Slf4j
public class Ext01ApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Map<String, Object> systemProperties = applicationContext.getEnvironment().getSystemProperties();
        log.info("【Ext01】---------------start------------------");
        systemProperties.forEach((key, value) -> {
//            log.info("【Ext01】---------------key = {}, value = {}------------------", key, value);
        });
        log.info("【Ext01】---------------end------------------");
    }
}
