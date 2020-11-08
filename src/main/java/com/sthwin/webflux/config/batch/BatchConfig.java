package com.sthwin.webflux.config.batch;

import com.sthwin.webflux.vo.MpisScheduleVo;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <code>@EnableBatchProcessing</code>을 사용하면 SimpleBatchConfiguration 클래스가 빈으로 등록되는데
 * 해당 클래스 내부를 보면 JobLauncher 와 JobRepositoy 를 빈으로 등록하는 소스를 볼 수 있음. 그대로 사용할 경우
 * JobRepository 의 IsolationLevel 부분에서 오류가 발생하기 때문에 그대로 사용할 수 없으므로,
 * 여기에 맞는 JobLauncher 와 JobRepositoy 를 빈으로 등록함.
 * <p>
 * <p>
 * Created by sthwin on 2020/10/31 5:42 오후
 */


@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSourceTransactionManager mpisTransactionManager;
    private final SqlSessionFactory mpisSqlSessionFactory;
    private final JobExecutionNotificationListener jobExecutionNotificationListener;
    private final StepExecutionNotificationListener stepExecutionNotificationListener;

    /**
     * 배치의 기본 설정 변경은 아래의 메소드내에서 진행한다.
     * <p>
     * 스프링 배치는 기본설정을 BatchConfigurer 인터페이스를 통해 진행한다.
     * <p>
     * BatchConfigurer 인터페이스를 통해 변경할 수 있는 기본 설정은 다음과 같다.
     * <pre>
     * JobRepository getJobRepository() throws Exception;
     * PlatformTransactionManager getTransactionManager() throws Exception;
     * JobLauncher getJobLauncher() throws Exception;
     * JobExplorer getJobExplorer() throws Exception;
     * </pre>
     *
     * @param dataSource
     * @param mpisTransactionManager
     * @return
     */
    @Bean
    public BatchConfigurer configurer(
            @Qualifier("mpisDataSource") DataSource dataSource,
            @Qualifier("mpisTransactionManager") DataSourceTransactionManager mpisTransactionManager) {
        return new DefaultBatchConfigurer() {

            @Override
            protected JobRepository createJobRepository() throws Exception {
                JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
                factory.setDataSource(dataSource);
                factory.setTransactionManager(mpisTransactionManager);
                factory.setTablePrefix("bt_mpis_");
                factory.afterPropertiesSet();
                return factory.getObject();
            }
        };
    }

    /**
     * 파일을 읽어 들인 후, db 에 저장한다.
     *
     * @return org.springframework.batch.core.Job
     */
    public Job createScheduleInsertJob() {
        return jobBuilderFactory.get("MPISScheduleInsertJob-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                .start(createInsertMpisScheduleStep())
                .listener(jobExecutionNotificationListener)
                .build();
    }

    public Step createInsertMpisScheduleStep() {
        return stepBuilderFactory.get("insertMPISScheduleStep")
                .<MpisScheduleVo, MpisScheduleVo>chunk(100)
                .reader(reader())
                .writer(writer())
                .transactionManager(mpisTransactionManager)
                .listener(stepExecutionNotificationListener)
                .build();
    }

    public MyBatisBatchItemWriter<MpisScheduleVo> writer() {
        return new MyBatisBatchItemWriterBuilder()
                .sqlSessionFactory(mpisSqlSessionFactory)
                .statementId("com.sthwin.webflux.mapper.MpisScheduleMapper.insert")
                .build();
    }

    public FlatFileItemReader<MpisScheduleVo> reader() {
        return new FlatFileItemReaderBuilder<MpisScheduleVo>()
                .name("MPISScheduleItemReader")
                .resource(new FileSystemResource("input/2017-12-04-22-00-02-MMPABC_20171204215956.2.87-0-1.mr"))
                .lineMapper((line, lineNumber) -> {
                    LineTokenizer tokenizer = new DelimitedLineTokenizer("^");
                    FieldSet fieldSet = tokenizer.tokenize(line);

                    return MpisScheduleVo.builder()
                            .agtCd(fieldSet.readString(2))
                            .depCityCd1(fieldSet.readString(7))
                            .arrCityCd1(fieldSet.readString(10))
                            .build();
                })
                .build();
    }
}
