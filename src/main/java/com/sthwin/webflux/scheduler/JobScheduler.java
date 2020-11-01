package com.sthwin.webflux.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by sthwin on 2020/11/01 3:11 오후
 */

@EnableScheduling
@RequiredArgsConstructor
@Component
public class JobScheduler {

    private final JobLauncher mpisJobLauncher;
    private final Job mpisScheduleInsertJob;

    /**
     * initialDelay 를 0으로 설정하여 즉시 실행시킴.
     * run every 1000000 msec (i.e., 매 10)
     */
    @Scheduled(initialDelay = 0, fixedRate = 1000000)
    public void runJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        JobExecution execution = mpisJobLauncher.run(mpisScheduleInsertJob, params);
    }
}
