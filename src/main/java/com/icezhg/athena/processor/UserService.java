package com.example.service;

import com.example.annotation.ExceptionHandler;
import com.example.handler.ExceptionRecorder;

@ExceptionHandler("全局异常处理")
public class UserService {
    
    private String serviceName = "UserService";
    
    public void processUser(String username) {
        System.out.println("处理用户: " + username);
        if ("error".equals(username)) {
            throw new RuntimeException("处理用户时发生错误: " + username);
        }
    }
    
    @ExceptionHandler("特定的除法方法")
    public int divide(int a, int b) {
        System.out.println("计算: " + a + " / " + b);
        return a / b; // 可能抛出ArithmeticException
    }
    
    private void privateMethod() {
        System.out.println("这是私有方法");
        throw new IllegalStateException("私有方法异常");
    }
    
    public void callPrivateMethod() {
        privateMethod();
    }
    
    public String getServiceName() {
        return serviceName;
    }
}

// 另一个测试类 - 只有方法级别注解
class Calculator {
    
    @ExceptionHandler
    public int add(int a, int b) {
        System.out.println("加法: " + a + " + " + b);
        return a + b;
    }
    
    @ExceptionHandler
    public int subtract(int a, int b) {
        System.out.println("减法: " + a + " - " + b);
        if (a < b) {
            throw new IllegalArgumentException("被减数不能小于减数");
        }
        return a - b;
    }
    
    public int multiply(int a, int b) {
        System.out.println("乘法: " + a + " * " + b);
        return a * b;
    }
}