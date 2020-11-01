package com.sthwin.webflux.config.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

/**
 * Created by sthwin on 2020/10/31 6:08 오후
 */
@Slf4j
@Component
public class JobExecutionNotificationListener extends JobExecutionListenerSupport {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("JobCompletionNotificationListener > beforeJob > " + jobExecution.getJobId());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("JobCompletionNotificationListener > afterJob");
        jobExecution.setExitStatus(ExitStatus.COMPLETED);
    }
}
