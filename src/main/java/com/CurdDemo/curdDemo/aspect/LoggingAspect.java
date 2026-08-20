package com.CurdDemo.curdDemo.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* com.CurdDemo.curdDemo.service.StudentServiceImpl.getStudentDetailsbyId(..))")
    public void logBeforeMethod(){
        System.out.println("This is Aspect call !!!!! 🤡🤡🤡🤡");
    }


    @AfterReturning("execution(* com.CurdDemo.curdDemo.service.StudentServiceImpl.getStudentDetailsbyId(..))")
    public void logAfterMethod(){
        System.out.println("This is Aspect call After !!!!! 📈📈📈📈");
    }

}
