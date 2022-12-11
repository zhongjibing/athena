package com.icezhg.athena.quartz.job;

import com.icezhg.athena.constant.Constants;
import com.icezhg.athena.domain.TaskLog;
import com.icezhg.athena.service.monitor.TaskLogService;
import com.icezhg.athena.service.monitor.TaskService;
import com.icezhg.athena.util.ApplicationContextUtil;
import com.icezhg.athena.vo.TaskInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public abstract class QuartzJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        long taskId = jobDataMap.getLong("taskId");
        getLogger().info("exec task: {}", taskId);

        TaskLog taskLog = createTaskLog(taskId);

        TaskInfo taskInfo = null;
        try {
            TaskService taskService = ApplicationContextUtil.getBean(TaskService.class);
            taskInfo = taskService.findById(taskId);
            if (taskInfo == null) {
                throw new JobExecutionException("task info not exist. id=" + taskId);
            }

            executeInternal(taskInfo);

            successLog(taskLog, taskInfo);
        } catch (JobExecutionException ex) {
            errorLog(taskLog, taskInfo, ex);
            throw ex;
        } finally {
            saveTaskLog(taskLog);
        }
    }

    protected abstract void executeInternal(TaskInfo taskInfo) throws JobExecutionException;

    private Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    private TaskLog createTaskLog(long taskId) {
        TaskLog taskLog = new TaskLog();
        taskLog.setTaskId(taskId);
        Date now = new Date();
        taskLog.setStartTime(now);
        taskLog.setCreateTime(now);
        return taskLog;
    }

    private void successLog(TaskLog taskLog, TaskInfo taskInfo) {
        taskLog.setStopTime(new Date());
        taskLog.setStatus(Constants.NORMAL);
        taskLog.setInvokeTarget(taskInfo.getInvokeTarget());
        long cost = taskLog.getStopTime().getTime() - taskLog.getStartTime().getTime();
        String message = String.format("task is successfully executed, taking %d ms.", cost);
        taskLog.setMessage(message);
        getLogger().info("[{}] {}", taskLog.getTaskId(), message);
    }

    private void errorLog(TaskLog taskLog, TaskInfo taskInfo, JobExecutionException ex) {
        taskLog.setStopTime(new Date());
        taskLog.setStatus(Constants.EXCEPTION);
        if (taskInfo != null) {
            taskLog.setInvokeTarget(taskInfo.getInvokeTarget());
        }

        long cost = taskLog.getStopTime().getTime() - taskLog.getStartTime().getTime();
        String message = String.format("task is failed to execute, taking %d ms.", cost);
        taskLog.setMessage(message);
        String exceptionInfo = ExceptionUtils.getStackTrace(ex);
        taskLog.setExceptionInfo(StringUtils.substring(exceptionInfo, 0, 2000));
        getLogger().info("[{}] {}\n{}", taskLog.getTaskId(), message, exceptionInfo);
    }

    private void saveTaskLog(TaskLog taskLog) {
        ApplicationContextUtil.getBean(TaskLogService.class).addLog(taskLog);
    }
}
