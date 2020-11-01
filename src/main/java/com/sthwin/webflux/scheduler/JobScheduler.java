package com.sthwin.webflux.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
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
    //@Scheduled(fixedRate = 1000000)
    @Scheduled(cron = "* 1 * * * * ")
    public void runJob() throws Exception {
        Map<String, JobParameter> param = new HashMap<>();
        param.put("startTime",
                new JobParameter(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                        , true));
        JobExecution execution = mpisJobLauncher.run(mpisScheduleInsertJob, new JobParameters(param));
    }
}
