package com.test.demo.aspect;

import com.test.demo.annotation.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("@annotation(com.test.demo.annotation.Log) || @within(com.test.demo.annotation.Log)")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        Log logAnnotation = method.getAnnotation(Log.class);
        if (logAnnotation == null) {
            logAnnotation = joinPoint.getTarget().getClass().getAnnotation(Log.class);
        }

        if (logAnnotation == null) {
            return joinPoint.proceed();
        }

        String methodName = method.getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String level = logAnnotation.level();

        if (logAnnotation.includeArgs()) {
            log(level, "Executing {}.{} with arguments: {}", className, methodName, Arrays.toString(joinPoint.getArgs()));
        } else {
            log(level, "Executing {}.{}", className, methodName);
        }

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        if (logAnnotation.includeResult()) {
            log(level, "{}.{} finished in {}ms with result: {}", className, methodName, (endTime - startTime), result);
        } else {
            log(level, "{}.{} finished in {}ms", className, methodName, (endTime - startTime));
        }

        return result;
    }

    private void log(String level, String message, Object... args) {
        switch (level.toUpperCase()) {
            case "DEBUG":
                logger.debug(message, args);
                break;
            case "WARN":
                logger.warn(message, args);
                break;
            case "ERROR":
                logger.error(message, args);
                break;
            default:
                logger.info(message, args);
                break;
        }
    }
}
