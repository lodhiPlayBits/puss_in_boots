package com.CurdDemo.curdDemo.service;

import org.springframework.stereotype.Component;

@Component
public class LoggingServiceUtil {
    public static void logStart(String className, String methodName) {
        System.out.println("Executing -> " + className + " : " + methodName);
    }

    public static void logEnd(String className, String methodName) {
        System.out.println("Finishing -> " + className + " : " + methodName);
    }


}
