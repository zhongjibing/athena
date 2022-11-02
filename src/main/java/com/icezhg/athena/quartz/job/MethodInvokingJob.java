package com.icezhg.athena.quartz.job;

import com.icezhg.athena.service.monitor.TaskService;
import com.icezhg.athena.util.ApplicationContextUtil;
import com.icezhg.athena.vo.TaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MethodInvokingJob extends QuartzJob {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        long taskId = jobDataMap.getLong("taskId");
        log.info("exec task: {}", taskId);

        TaskService taskService = ApplicationContextUtil.getBean(TaskService.class);
        TaskInfo taskInfo = taskService.findById(taskId);
        if (taskInfo == null) {
            throw new JobExecutionException("task info not exist. id=" + taskId);
        }

        try {
            String invokeTarget = taskInfo.getInvokeTarget();
            String executableName = extractExecutableName(invokeTarget);
            String methodName = extractMethodName(invokeTarget);
            List<Parameter> parameters = extractParameters(invokeTarget);
            invokeTaskMethod(executableName, methodName, parameters);
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    private void invokeTaskMethod(String executableName, String methodName, List<Parameter> parameters) throws Exception {
        String paramStr = StringUtils.join(parameters.stream().map(Parameter::toString).toList(), ",");
        log.info("try exec task: {}.{}({})", executableName, methodName, paramStr);

        Object target;
        try {
            target = ApplicationContextUtil.getBean(executableName);
        } catch (Exception ex1) {
            try {
                target = Class.forName(executableName).getDeclaredConstructor().newInstance();
            } catch (Exception ex2) {
                throw new Exception("executable instance not found: " + executableName);
            }
        }

        Class<?>[] paramTypes = parameters.stream().map(Parameter::type).toArray(Class<?>[]::new);
        Method method = ReflectionUtils.findMethod(target.getClass(), methodName, paramTypes);
        Assert.notNull(method, "method not found: " + methodName);

        ReflectionUtils.makeAccessible(method);
        Object[] args = parameters.stream().map(Parameter::value).toArray();
        ReflectionUtils.invokeMethod(method, target, args);
    }


    private String extractExecutableName(String invokeTarget) {
        return StringUtils.substringBeforeLast(StringUtils.substringBefore(invokeTarget, "("), ".");
    }

    private String extractMethodName(String invokeTarget) {
        return StringUtils.substringAfterLast(StringUtils.substringBefore(invokeTarget, "("), ".");
    }

    private List<Parameter> extractParameters(String invokeTarget) {
        if (StringUtils.containsNone(invokeTarget, "()")) {
            return List.of();
        }

        String paramStr = StringUtils.substringBetween(invokeTarget, "(", ")");
        if (paramStr == null) {
            throw new IllegalArgumentException("invalid parameters: " + invokeTarget);
        }

        return Arrays.stream(paramStr.split(",\s*")).map(item -> {
            if (StringUtils.equalsAnyIgnoreCase(item, "true", "false")) {
                return new Parameter(boolean.class, Boolean.parseBoolean(item));
            } else if (StringUtils.startsWith(item, "'") && StringUtils.endsWith(item, "'")) {
                return new Parameter(String.class, StringUtils.substringBetween(item, "'"));
            } else if (StringUtils.startsWith(item, "\"") && StringUtils.endsWith(item, "\"")) {
                return new Parameter(String.class, StringUtils.substringBetween(item, "\""));
            } else if (NumberUtils.isCreatable(item)) {
                Number number = NumberUtils.createNumber(item);
                return new Parameter(number.getClass(), number);
            } else {
                throw new IllegalArgumentException("invalid parameter: " + item);
            }
        }).collect(Collectors.toList());
    }

    private record Parameter(Class<?> type, Object value) {
        @Override
        public String toString() {
            return type == String.class ? "\"" + value + "\"" : String.valueOf(value);
        }
    }
}
