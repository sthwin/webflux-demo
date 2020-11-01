package com.sthwin.webflux.config.batch;

import com.sthwin.webflux.vo.MPISScheduleVo;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by sthwin on 2020/10/31 5:42 오후
 */


@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@EnableScheduling
public class BatchConfig {

    public final JobBuilderFactory jobBuilderFactory;
    public final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job createSimpleJob(JobCompletionNotificationListener listener) {
        return null;
    }

    @Bean
    public Step creteSimpleStep1() {
        return stepBuilderFactory.get("simpleStep1")
                .<MPISScheduleVo, MPISScheduleVo>chunk(100)
                .reader(headReader())
                .build();
    }

    @Bean
    public FlatFileItemReader<MPISScheduleVo> headReader() {
        return new FlatFileItemReaderBuilder<MPISScheduleVo>()
                .name("MPISScheduleItemReader")
                .comments("")
                .resource(new FileSystemResource("input/2017-12-04-22-00-02-MMPABC_20171204215956.2.87-0-1/2017-12-04-22-00-02-MMPABC_20171204215956.2.87-0-1.mr"))
                .delimited()
                .delimiter("^").names()
//                .lineMapper((line, lineNumber) -> {
//                    return MPISScheduleVo.builder().build();
//                })
                .fieldSetMapper(fieldSet -> {
                    return  MPISScheduleVo.builder()
                            .agtCd(fieldSet.readString(2))
                            .depCityCd1(fieldSet.readString(7))
                            .arrCityCd1(fieldSet.readString(10))
                            .build();
                })
                .build();
    }
}
