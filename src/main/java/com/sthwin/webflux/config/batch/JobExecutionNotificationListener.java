package com.sthwin.webflux.config.batch;

import lombok.extern.slf4j.Slf4j;
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
        log.trace("JobCompletionNotificationListener > beforeJob");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.trace("JobCompletionNotificationListener > afterJob");
    }
}
