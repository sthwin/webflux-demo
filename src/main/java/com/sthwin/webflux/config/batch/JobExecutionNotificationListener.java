package com.sthwin.webflux.config.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sthwin on 2020/10/31 6:08 오후
 */
@Slf4j
@Component
public class JobExecutionNotificationListener extends JobExecutionListenerSupport {

    @Autowired
    private JobRegistry jobRegistry;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("JobCompletionNotificationListener > beforeJob > " + jobExecution.getJobId());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("JobCompletionNotificationListener > afterJob");
        jobRegistry.unregister(jobExecution.getJobInstance().getJobName());
        jobExecution.setExitStatus(ExitStatus.COMPLETED);
    }
}
