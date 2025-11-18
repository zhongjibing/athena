package com.example.factory;

import com.example.enhancer.ExceptionHandlerEnhancer;

public class EnhancedObjectFactory {
    
    @SuppressWarnings("unchecked")
    public static <T> T createEnhancedInstance(Class<T> clazz) throws Exception {
        T instance = clazz.newInstance();
        return (T) ExceptionHandlerEnhancer.enhance(instance);
    }
    
    public static <T> T createEnhancedInstance(T instance) {
        return ExceptionHandlerEnhancer.enhance(instance);
    }
}