// RuntimeClassRedefiner.java
package com.example.trace.enhancer;

import com.example.trace.Trace;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.*;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class RuntimeClassRedefiner {
    
    static {
        // 安装 Byte Buddy Agent
        ByteBuddyAgent.install();
    }
    
    public static void enhanceAnnotatedClasses() {
        Instrumentation inst = ByteBuddyAgent.getInstrumentation();
        
        // 查找所有被 @Trace 注解的类并增强
        for (Class<?> clazz : findAllTraceAnnotatedClasses()) {
            enhanceClass(clazz, inst);
        }
    }
    
    public static void enhanceClass(Class<?> clazz, Instrumentation inst) {
        try {
            DynamicType.Unloaded<?> enhanced = new ByteBuddy()
                .redefine(clazz)
                .method(ElementMatchers.isPublic()
                        .and(ElementMatchers.not(ElementMatchers.isDeclaredBy(Object.class))))
                .intercept(MethodDelegation.to(TraceInterceptor.class))
                .make();
            
            // 重定义原始类
            ClassReloadingStrategy.fromInstalledAgent(inst)
                .load(enhanced.getTypeDescription(), enhanced.getBytes());
            
        } catch (Exception e) {
            System.err.println("Failed to enhance class: " + clazz.getName() + ", " + e.getMessage());
        }
    }
    
    private static Class<?>[] findAllTraceAnnotatedClasses() {
        // 实现类路径扫描，查找所有 @Trace 注解的类
        // 这里简化实现，实际使用时需要完整的类路径扫描
        return new Class<?>[0];
    }
    
    public static class TraceInterceptor {
        @RuntimeType
        public static Object intercept(
            @Origin Method method,
            @This Object self,
            @AllArguments Object[] args,
            @SuperCall Callable<?> callable) throws Exception {
            
            String methodName = method.getName();
            System.out.println("[TRACE] Enter: " + methodName);
            
            try {
                Object result = callable.call();
                System.out.println("[TRACE] Exit: " + methodName + " -> " + result);
                return result;
            } catch (Exception e) {
                System.err.println("[TRACE] Error in " + methodName + ": " + e.getMessage());
                throw e;
            }
        }
    }
}