package com.sthwin.webflux.config.batch;

import com.sthwin.webflux.mapper.MpisDataFeeMapper;
import com.sthwin.webflux.vo.MpisDataFeed;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <code>@EnableBatchProcessing</code>을 사용하면 SimpleBatchConfiguration 클래스가 빈으로 등록되는데
 * 해당 클래스 내부를 보면 JobLauncher 와 JobRepositoy 를 빈으로 등록하는 소스를 볼 수 있음. 그대로 사용할 경우
 * JobRepository 의 IsolationLevel 부분에서 오류가 발생하기 때문에 그대로 사용할 수 없으므로,
 * 여기에 맞는 JobLauncher 와 JobRepositoy 를 빈으로 등록함.
 * <p>
 * <p>
 * Created by sthwin on 2020/10/31 5:42 오후
 */

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private DataSource mpisDataSource;
    @Autowired
    private DataSourceTransactionManager mpisTransactionManager;
    @Autowired
    private JobRegistry jobRegistry;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private MpisDataFeeMapper mpisDataFeeMapper;

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
     * @return
     */
    @Bean
    public BatchConfigurer configurer() {
        return new DefaultBatchConfigurer(mpisDataSource) {

            @Override
            protected JobRepository createJobRepository() throws Exception {
                JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
                factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
                factory.setDataSource(mpisDataSource);
                factory.setTransactionManager(mpisTransactionManager);
                factory.setTablePrefix("bt_mpis_");
                factory.afterPropertiesSet();
                return factory.getObject();
            }

            @SneakyThrows
            @Override
            public JobExplorer getJobExplorer() {
                JobExplorerFactoryBean factoryBean = new JobExplorerFactoryBean();
                factoryBean.setDataSource(mpisDataSource);
                factoryBean.setTablePrefix("bt_mpis_");
                factoryBean.afterPropertiesSet();
                return factoryBean.getObject();
            }

            @Override
            protected JobLauncher createJobLauncher() throws Exception {
                SimpleJobLauncher launcher = new SimpleJobLauncher();
                launcher.setJobRepository(jobRepository);
                SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
                executor.setConcurrencyLimit(10);
                launcher.setTaskExecutor(executor);
                launcher.afterPropertiesSet();
                return launcher;
            }
        };
    }

    /**
     * Job 이 빈으로 등록될 경우, jobRegistry 에 job 을 등록 시킨다.
     *
     * @return
     */
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }


    @Bean
    public SimpleJobOperator jobOperator(JobExplorer jobExplorer,
                                         JobRepository jobRepository,
                                         JobRegistry jobRegistry,
                                         JobLauncher jobLauncher) {

        SimpleJobOperator jobOperator = new SimpleJobOperator();
        jobOperator.setJobExplorer(jobExplorer);
        jobOperator.setJobRepository(jobRepository);
        jobOperator.setJobRegistry(jobRegistry);
        jobOperator.setJobLauncher(jobLauncher);
        return jobOperator;
    }

    @Bean
    public Job testJob() throws IOException {
        return jobBuilderFactory.get("testJob")
               // .incrementer(new RunIdIncrementer())
                .preventRestart()
                .listener(new JobExecutionListenerSupport() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        Queue<String> list = new ConcurrentLinkedQueue<>();
                        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("input"))) {
                            for (Path path : stream) {
                                if (!Files.isDirectory(path)) {
                                    list.add(path.getFileName().toString());
                                }
                            }
                            jobExecution.getExecutionContext().put("LIST", list);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .start(testStep()).build();
    }

    public Step testStep() throws IOException {
        return stepBuilderFactory.get("test")
                .tasklet((contribution, chunkContext) -> {
                    Queue<String>  list = (Queue<String>)  contribution.getStepExecution().getJobExecution().getExecutionContext().get("LIST");
                    if (list == null || list.isEmpty())
                        return RepeatStatus.FINISHED;

                    String filename = list.poll();
                    //list.remove(0);
                    MpisDataFeed vo = new MpisDataFeed();
                    vo.setFileName(filename);
                    mpisDataFeeMapper.insert(vo);
                    return RepeatStatus.CONTINUABLE;
                })
                .listener(new StepExecutionListenerSupport() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                    }
                })
                .startLimit(1)
               // .transactionManager(mpisTransactionManager)
                .build();
    }
}
