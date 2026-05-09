package com.test.demo.annotation;

import com.test.demo.validation.PanValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PanValidator.class)
@Documented
public @interface ValidPan {
    String message() default "Invalid PAN card number format. Expected format: [A-Z]{5}[0-9]{4}[A-Z]{1}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
