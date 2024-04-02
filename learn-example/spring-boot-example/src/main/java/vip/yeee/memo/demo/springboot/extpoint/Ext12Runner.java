package vip.yeee.memo.demo.springboot.extpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 扩展点12：XXXRunner
 * Springboot的扩展点
 *
 * 1. 执行时机：Spring容器、Tomcat容器正式启动完成后，可以正式处理业务请求前，即项目启动的最后一步
 *
 * SpringApplication.callRunners(context, applicationArguments) ->
 *
 * 1.1 CommandLineRunner.run()的执行时机要晚于ApplicationRunner.run()
 *
 * 1.2 与之前其他的扩展点不同的是，其执行时机最晚，即在Spring容器、Tomcat容器正式启动完成的最后一步
 *
 * 2. 应用
 * 项目启动前，热点数据的预加载、清除临时文件、读取自定义配置信息等；
 *
 *
 * @author https://www.yeee.vip
 * @since 2023/8/14 10:01
 */
@Slf4j
@Component
public class Ext12Runner implements CommandLineRunner, ApplicationRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("【Ext12】--------CommandLineRunner.run-------start------------------");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("【Ext12】--------ApplicationRunner.run-------start------------------");
    }
}
