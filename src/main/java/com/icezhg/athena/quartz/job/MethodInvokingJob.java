package com.icezhg.athena.quartz.job;

import com.icezhg.athena.service.monitor.TaskService;
import com.icezhg.athena.util.ApplicationContextUtil;
import com.icezhg.athena.vo.TaskInfo;
import com.icezhg.authorization.core.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
public class MethodInvokingJob extends QuartzJob {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        long taskId = jobDataMap.getLong("taskId");
        log.info("exec task: {}", taskId);

        TaskService taskService = ApplicationContextUtil.getBean(TaskService.class);
        TaskInfo taskInfo = taskService.findById(taskId);
        log.info("task detail: {}", JsonUtil.toJson(taskInfo));
    }
}
