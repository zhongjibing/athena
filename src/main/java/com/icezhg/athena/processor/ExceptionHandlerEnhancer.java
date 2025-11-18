package com.example.enhancer;

import com.example.annotation.ExceptionHandler;
import com.example.handler.ExceptionRecorder;
import com.example.handler.ExceptionRecorderImpl;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class ExceptionHandlerEnhancer {
    
    public static <T> T enhance(T target) {
        Class<?> targetClass = target.getClass();
        
        // 检查类或方法是否有ExceptionHandler注解
        if (!hasExceptionHandlerAnnotation(targetClass)) {
            return target;
        }
        
        try {
            DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
                .subclass(targetClass)
                .name(targetClass.getName() + "$$EnhancedByByteBuddy")
                // 添加异常记录器字段
                .defineField("exceptionRecorder", ExceptionRecorder.class, Visibility.PRIVATE)
                // 实现ExceptionRecorder接口的方法
                .implement(ExceptionRecorder.class)
                .intercept(FieldAccessor.ofBeanProperty())
                // 拦截所有方法
                .method(ElementMatchers.isMethod()
                        .and(ElementMatchers.not(ElementMatchers.isDeclaredBy(Object.class)))
                        .and(ElementMatchers.not(ElementMatchers.isDeclaredBy(ExceptionRecorder.class))))
                .intercept(MethodDelegation.to(ExceptionInterceptor.class))
                .make();
            
            @SuppressWarnings("unchecked")
            Class<T> enhancedClass = (Class<T>) dynamicType.load(
                targetClass.getClassLoader(), 
                ClassLoadingStrategy.Default.INJECTION
            ).getLoaded();
            
            T enhancedInstance = enhancedClass.newInstance();
            // 设置异常记录器
            Method setRecorder = enhancedClass.getMethod("setExceptionRecorder", ExceptionRecorder.class);
            setRecorder.invoke(enhancedInstance, new ExceptionRecorderImpl());
            
            // 复制原始对象的状态
            copyFieldValues(target, enhancedInstance);
            
            return enhancedInstance;
        } catch (Exception e) {
            throw new RuntimeException("增强类失败: " + targetClass.getName(), e);
        }
    }
    
    private static boolean hasExceptionHandlerAnnotation(Class<?> clazz) {
        // 检查类级别的注解
        if (clazz.isAnnotationPresent(ExceptionHandler.class)) {
            return true;
        }
        
        // 检查方法级别的注解
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(ExceptionHandler.class)) {
                return true;
            }
        }
        
        return false;
    }
    
    private static void copyFieldValues(Object source, Object target) throws Exception {
        Class<?> clazz = source.getClass();
        while (clazz != null && clazz != Object.class) {
            for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    Object value = field.get(source);
                    field.set(target, value);
                }
            }
            clazz = clazz.getSuperclass();
        }
    }
    
    // 方法拦截器
    public static class ExceptionInterceptor {
        
        @RuntimeType
        public static Object intercept(@This Object self,
                                     @Origin Method method,
                                     @AllArguments Object[] args,
                                     @SuperCall Callable<?> callable) throws Exception {
            ExceptionRecorder recorder = ((ExceptionRecorder) self).getExceptionRecorder();
            
            // 检查方法是否有ExceptionHandler注解
            boolean hasMethodAnnotation = method.isAnnotationPresent(ExceptionHandler.class);
            
            // 检查类是否有ExceptionHandler注解
            boolean hasClassAnnotation = self.getClass().getSuperclass().isAnnotationPresent(ExceptionHandler.class);
            
            // 如果方法或类有注解，则进行异常处理
            if (hasMethodAnnotation || hasClassAnnotation) {
                try {
                    return callable.call();
                } catch (Exception e) {
                    // 记录异常信息
                    recorder.recordException(e, method.getName(), args);
                    // 继续抛出异常
                    throw e;
                }
            } else {
                // 没有注解的方法直接调用
                return callable.call();
            }
        }
    }
}