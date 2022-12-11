package com.icezhg.athena.quartz.job;

import com.icezhg.athena.util.ApplicationContextUtil;
import com.icezhg.athena.vo.TaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.quartz.JobExecutionException;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Slf4j
public class MethodInvokingJob extends QuartzJob {

    @Override
    protected void executeInternal(TaskInfo taskInfo) throws JobExecutionException {
        try {
            String invokeTarget = taskInfo.getInvokeTarget();
            List<String> args = splitInvokeTarget(invokeTarget);
            invokeTaskMethod(args.get(0), args.get(1), parseParameter(args.subList(2, args.size())));
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

    private static List<String> splitInvokeTarget(String invokeTarget) {
        List<String> args = new ArrayList<>();
        Stack<Character> stack = new Stack<>();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < invokeTarget.length(); i++) {
            char c = invokeTarget.charAt(i);
            if (c == '(') {
                if (!stack.isEmpty()) {
                    throw new IllegalArgumentException("bad invokeTarget");
                }

                String executeTarget = popString(builder);
                String executableName = StringUtils.substringBeforeLast(executeTarget, ".");
                String methodName = StringUtils.substringAfterLast(executeTarget, ".");
                if (StringUtils.isBlank(executableName) || StringUtils.isBlank(methodName)) {
                    throw new IllegalArgumentException("bad invokeTarget");
                }
                args.add(executableName);
                args.add(methodName);

                stack.push(c);
            } else if (c == ')') {
                if (!stack.isEmpty() && stack.peek() == '(') {
                    if (!builder.isEmpty()) {
                        args.add(popString(builder).trim());
                    }
                    stack.pop();

                    if (StringUtils.isNotBlank(StringUtils.substring(invokeTarget, i + 1))) {
                        throw new IllegalArgumentException("bad invokeTarget");
                    }
                } else {
                    builder.append(c);
                }
            } else if (c == '\'') {
                if (!stack.isEmpty()) {
                    if (stack.peek() == '\'') {
                        args.add('\'' + popString(builder) + '\'');
                        stack.pop();
                    } else if (stack.peek() == '"') {
                        builder.append(c);
                    } else {
                        if (!builder.isEmpty()) {
                            args.add(popString(builder).trim());
                        }
                        stack.push(c);
                    }
                } else {
                    stack.push(c);
                }
            } else if (c == '"') {
                if (!stack.isEmpty()) {
                    if (stack.peek() == '"') {
                        args.add('"' + popString(builder) + '"');
                        stack.pop();
                    } else if (stack.peek() == '\'') {
                        builder.append(c);
                    } else {
                        if (!builder.isEmpty()) {
                            args.add(popString(builder).trim());
                        }
                        stack.push(c);
                    }
                } else {
                    stack.push(c);
                }
            } else if (c == ' ') {
                if ((!stack.isEmpty() && stack.peek() != '(') || !builder.isEmpty()) {
                    builder.append(c);
                }
            } else if (c == ',') {
                if (!builder.isEmpty()) {
                    args.add(popString(builder).trim());
                }
            } else {
                builder.append(c);
            }
        }

        if (stack.isEmpty() && args.isEmpty() && !builder.isEmpty()) {
            String executeTarget = popString(builder);
            String executableName = StringUtils.substringBeforeLast(executeTarget, ".");
            String methodName = StringUtils.substringAfterLast(executeTarget, ".");
            if (StringUtils.isBlank(executableName) || StringUtils.isBlank(methodName)) {
                throw new IllegalArgumentException("bad invokeTarget");
            }
            args.add(executableName);
            args.add(methodName);
        }


        if (!stack.isEmpty() || !builder.isEmpty() || args.size() < 2) {
            throw new IllegalArgumentException("bad invokeTarget");
        }

        return args;
    }

    private static String popString(StringBuilder builder) {
        String str = builder.toString();
        builder.delete(0, builder.length());
        return str;
    }

    private static List<Parameter> parseParameter(List<String> parameterStrings) {
        return parameterStrings.stream().map(item -> {
            if (StringUtils.equalsAnyIgnoreCase(item, "true", "false")) {
                return new Parameter(boolean.class, Boolean.parseBoolean(item));
            } else if (StringUtils.startsWith(item, "'") && StringUtils.endsWith(item, "'")) {
                return new Parameter(String.class, StringUtils.substring(item, 1, item.length() - 1));
            } else if (StringUtils.startsWith(item, "\"") && StringUtils.endsWith(item, "\"")) {
                return new Parameter(String.class, StringUtils.substring(item, 1, item.length() - 1));
            } else if (NumberUtils.isCreatable(item)) {
                Number number = NumberUtils.createNumber(item);
                return new Parameter(number.getClass(), number);
            } else {
                return new Parameter(String.class, item);
            }
        }).toList();
    }

    private record Parameter(Class<?> type, Object value) {
        @Override
        public String toString() {
            return type.getName() + ": " + String.valueOf(value);
        }
    }

    public static void main(String[] args) {
        String target = "test.test.test('a\"1\"' , 1.0, '1L', 1L, lL, true , ''true'')";
        List<String> strings = splitInvokeTarget(target);
        System.out.println("1: " + strings);
        System.out.println("2: " + parseParameter(strings.subList(2, strings.size())));
    }
}
