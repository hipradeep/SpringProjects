package com.test.demo.aspect;

import com.test.demo.dto.UserDto;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Alternative AOP-based validation for PAN cards.
 * While Bean Validation is preferred for DTOs, this demonstrates how to use AOP
 * to intercept methods and validate arguments manually.
 */
@Aspect
@Component
public class PanValidationAspect {

    private static final String PAN_PATTERN = "[A-Z]{5}[0-9]{4}[A-Z]{1}";

    @Before("execution(* com.test.demo.controller..*(..)) && args(userDto,..)")
    public void validatePan(JoinPoint joinPoint, UserDto userDto) {
        String pan = userDto.getPanCard();
        if (pan == null || !Pattern.matches(PAN_PATTERN, pan)) {
            System.err.println("AOP Validation Failed for method: " + joinPoint.getSignature().getName());
            throw new IllegalArgumentException("AOP Validation Failed: Invalid PAN Card Format [" + pan + "]");
        }
        System.out.println("AOP Validation Succeeded for method: " + joinPoint.getSignature().getName());
    }
}
