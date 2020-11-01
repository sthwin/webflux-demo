package com.sthwin.webflux.config.batch;

import com.sthwin.webflux.util.TargetFileSelector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 처리할 파일을 stepexecutioncontext 에 담아서 step 에서 사용할 수 있도록 한다.
 *
 * Created by sthwin on 2020/11/01 6:03 오후
 */
@Slf4j
@Component
public class StepExecutionNotificationListener extends StepExecutionListenerSupport {

    public static final String TARGET_FILE = "TARGET_FILE"; // 처리할 대상 파일을 가져오기 위한 키 값

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.trace("afterStep" + stepExecution.getSummary());
        stepExecution.getExecutionContext().remove(TARGET_FILE);
        File target = TargetFileSelector.getTargetFile();
        if (target == null )  // 처리할 파일이 남아 있을 경우.
            return ExitStatus.COMPLETED;
        else
            return ExitStatus.EXECUTING;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.trace("beforeStep");
        File target = TargetFileSelector.getTargetFile();
        stepExecution.getExecutionContext().put(TARGET_FILE, target);
    }
}
