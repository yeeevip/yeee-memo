package com.yeee.exec.linux.sh;

import org.quartz.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.UUID;

/**
 * description ...
 *
 * @author yeeeee
 * @since 2021/12/27 14:04
 */
@Configuration
public class TestExecRemoteCommand {

    @Bean
    public CommandLineRunner setupTestExecCommand(Scheduler scheduler) {
        return args -> {

            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("host", "127.0.0.1");
            jobDataMap.put("port", "22");
            jobDataMap.put("user", "root");
            jobDataMap.put("password", "123456");
            String privateKey = "-----BEGIN OPENSSH PRIVATE KEY-----\n" +
                    "-----END OPENSSH PRIVATE KEY-----";
            jobDataMap.put("privateKey", privateKey);
            jobDataMap.put("command", "/home/yeee/test.sh");

            JobDetail job = JobBuilder.newJob(ExecShJob.class)
                    .withIdentity(UUID.randomUUID().toString(), "sh-jobs")
                    .withDescription("Test SH job")
                    .usingJobData(jobDataMap)
                    .storeDurably()
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(job)
                    .withIdentity(job.getKey().getName(), "sh-triggers")
                    .withDescription("Send Email Trigger")
                    .startAt(new Date())
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                    .build();
            scheduler.scheduleJob(job, trigger);
        };
    }

}
