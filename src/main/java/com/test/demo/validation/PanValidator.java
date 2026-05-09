package com.test.demo.validation;

import com.test.demo.annotation.ValidPan;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Why use Bean Validation (JSR 380) instead of AOP for validation?
 * 1. Standard Compliance: It follows the Java standard for validation, making it portable and well-understood.
 * 2. Integration: Integrates natively with Spring MVC to handle binding errors and return appropriate HTTP status codes (400 Bad Request) automatically.
 * 3. Granularity: Allows for field-level validation within DTOs/objects, whereas AOP is better suited for method-level cross-cutting concerns.
 * 4. Rich ecosystem: Provides built-in support for complex validation groups, internationalized messages, and recursive validation.
 */
public class PanValidator implements ConstraintValidator<ValidPan, String> {

    private static final String PAN_PATTERN = "[A-Z]{5}[0-9]{4}[A-Z]{1}";

    @Override
    public boolean isValid(String pan, ConstraintValidatorContext context) {
        if (pan == null || pan.isEmpty()) {
            return false;
        }
        return Pattern.matches(PAN_PATTERN, pan);
    }
}
