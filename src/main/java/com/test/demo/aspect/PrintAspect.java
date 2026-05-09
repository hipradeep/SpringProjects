package com.test.demo.aspect;

import com.test.demo.annotation.Print;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PrintAspect {

    @Before("@annotation(printAnnotation)")
    public void printMessage(JoinPoint joinPoint, Print printAnnotation) {
        System.out.println(printAnnotation.value());
    }
}
