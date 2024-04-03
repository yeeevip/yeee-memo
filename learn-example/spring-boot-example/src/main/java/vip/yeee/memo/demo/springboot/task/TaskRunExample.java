package vip.yeee.memo.demo.springboot.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2024/4/7 10:56
 */
@Slf4j
@Component
public class TaskRunExample {

    @Scheduled(cron = "0 0/1 * * * ?")
    public void taskAa() {
        log.info("taskAa RUN");
    }

    @Scheduled(cron = "20 * * * * ?")
    public void taskBb() {
        log.info("taskBb RUN");
    }
}
