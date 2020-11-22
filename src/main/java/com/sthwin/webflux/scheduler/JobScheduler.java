package com.sthwin.webflux.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.*;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    static int cnt = 0;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job testJob;
    @Autowired
    private JobOperator jobOperator;

    @PostConstruct
    public void init() {
        jobOperator.getJobNames().stream().forEach(
                name -> {
                    try {
                        jobOperator.getRunningExecutions(name).stream().forEach(
                                jobExeId -> {
                                    try {
                                        jobOperator.stop(jobExeId);
                                    } catch (NoSuchJobExecutionException e) {
                                        e.printStackTrace();
                                    } catch (JobExecutionNotRunningException e) {
                                        e.printStackTrace();
                                    }
                                }
                        );
                    } catch (NoSuchJobException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    @Scheduled(initialDelay = 0, fixedDelay = 5000)
    public void testSchedule() {
        System.out.println("실행횟수:" + cnt++);

        Map<String, JobParameter> param = new HashMap<>();
        param.put("startTime",
                new JobParameter(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                        , true));
        System.out.println("Job 실행:" + param.toString());
        JobExecution execution = null;
        try {
            execution = jobLauncher.run(testJob, new JobParameters(param));
            System.out.println(execution.getStatus());
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }
}
