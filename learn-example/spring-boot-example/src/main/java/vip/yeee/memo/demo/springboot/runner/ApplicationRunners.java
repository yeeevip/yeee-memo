package vip.yeee.memo.demo.springboot.runner;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * description......
 *
 * @author yeeee
 * @since 2023/5/18 9:24
 */
@Slf4j
@Component
public class ApplicationRunners {

    @Component
    public class RunnerTask1 implements ApplicationRunner {
        @Override
        public void run(ApplicationArguments args) throws Exception {
            Stopwatch stopwatch = Stopwatch.createStarted();
            log.info("【RunnerTask1】- 开始");
            Set<String> optionNames = args.getOptionNames();
            for (String optionName : optionNames) {
                List<String> values = args.getOptionValues(optionName);
                System.out.println(values.toString());
            }
            log.info("【RunnerTask1】- 结束，耗时：{}", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    // 顺序小的先执行
    @Order(0)
    @Component
    public class RunnerTask2 implements ApplicationRunner {
        @Override
        public void run(ApplicationArguments args) throws Exception {
            Stopwatch stopwatch = Stopwatch.createStarted();
            log.info("【RunnerTask2】- 开始");
            Set<String> optionNames = args.getOptionNames();
            for (String optionName : optionNames) {
                List<String> values = args.getOptionValues(optionName);
                System.out.println(values.toString());
            }
            log.info("【RunnerTask2】- 结束，耗时：{}", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        }
    }

}
