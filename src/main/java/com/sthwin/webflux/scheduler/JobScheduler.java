package com.sthwin.webflux.scheduler;

import com.sthwin.webflux.config.batch.BatchConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.core.explore.JobExplorer;
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

    private final JobLauncher jobLauncher;
    private final BatchConfig batchConfig;
    private final JobExplorer jobExplorer;
    private final JobRegistry jobRegistry;

    /**
     * initialDelay 를 0으로 설정하여 즉시 실행시킴.
     * run every 1000000 msec (i.e., 매 10)
     */
    @Scheduled(initialDelay = 0, fixedRate = 50000)
    //@Scheduled(cron = "1 * * * * * ")
    public void runJob() throws Exception {
        // 레지스트리에 Job 이 남아 있으면 종료되지 않은 것으로 보고 시작시키지 않는다.
        for(String name : jobRegistry.getJobNames()) {
            if(name.startsWith("MPISScheduleInsertJob-")) {
                System.out.println(name + "이 이미 실행 중 입니다.");
                return;
            }
        }
        Map<String, JobParameter> param = new HashMap<>();
        param.put("startTime",
                new JobParameter(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                        , true));
        Job job = batchConfig.createScheduleInsertJob();
        jobRegistry.register(new ReferenceJobFactory(job));
        jobLauncher.run(job, new JobParameters(param));
    }
}
