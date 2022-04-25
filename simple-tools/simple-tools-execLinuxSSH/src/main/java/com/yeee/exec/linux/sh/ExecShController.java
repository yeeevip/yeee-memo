package com.yeee.exec.linux.sh;

import com.yeee.exec.AbstractJobDefinition;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.quartz.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * description ...
 *
 * @author yeeeee
 * @since 2021/12/27 14:43
 */
@RestController
@RequiredArgsConstructor
public class ExecShController {

    private final Scheduler scheduler;

    @Getter
    @Setter
    static class JobDefinition extends AbstractJobDefinition {
        private String host;
        private Integer port;
        private String user;
        private String password;
        private String privateKey;
        private String command;
        private Long connectTimeout;
        private Long authTimeout;
        private Long openChannelTimeout;
        private Long executionTimeout;
    }

    @PostMapping("create/shJob")
    public ResponseEntity<JobDefinition> createJob(@RequestBody JobDefinition jobDefinition) throws SchedulerException {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("host", jobDefinition.getHost());
        jobDataMap.put("port", jobDefinition.getPort());
        jobDataMap.put("user", jobDefinition.getUser());
        jobDataMap.put("password", jobDefinition.getPassword());
        jobDataMap.put("privateKey", jobDefinition.getPrivateKey());
        jobDataMap.put("command", jobDefinition.getCommand());
        JobDetail job = JobBuilder.newJob(ExecShJob.class)
                .withIdentity(UUID.randomUUID().toString(), jobDefinition.getJobGroup())
                .withDescription(jobDefinition.getJobDescription())
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(job)
                .withIdentity(job.getKey().getName(), jobDefinition.getTriggerGroup())
                .withDescription(jobDefinition.getTriggerDescription())
                .startAt(Date.from(jobDefinition.getTriggerStartAt()))
                .withSchedule(CronScheduleBuilder.cronSchedule(jobDefinition.getTriggerCronExpression()))
                .build();
        scheduler.scheduleJob(job, trigger);
        return ResponseEntity.ok(jobDefinition);
    }

}
