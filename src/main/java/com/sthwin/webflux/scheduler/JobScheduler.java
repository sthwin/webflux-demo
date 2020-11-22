package com.sthwin.webflux.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sthwin on 2020/11/01 3:11 오후
 */
@Slf4j
@EnableScheduling
@Component
public class JobScheduler {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job testJob;

    @Scheduled(initialDelay = 0, fixedRate = 100000)
    public void testSchedule() throws Exception {
        Map<String, JobParameter> param = new HashMap<>();
        param.put("startTime",
                new JobParameter(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                        , true));
        System.out.println("Job 실행:" + param.toString());
        JobExecution execution = jobLauncher.run(testJob, new JobParameters(param));
        System.out.println(execution.getStatus());
    }
}
