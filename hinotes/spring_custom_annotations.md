# Spring Custom Annotations and AOP

This document provides a reference for creating and using custom annotations in Spring Boot using Aspect-Oriented Programming (AOP).

## Design Steps for Custom Annotations

Follow these steps to implement a custom functional annotation in Spring Boot:

1.  **Add AOP Dependency**: Include `spring-boot-starter-aop` in your `pom.xml` to enable Aspect-Oriented Programming.
2.  **Define the Annotation**:
    *   Use `@interface` to create the annotation.
    *   Apply `@Target` to define where it can be used (Methods, Types, etc.).
    *   Apply `@Retention(RetentionPolicy.RUNTIME)` so it's available at execution time.
3.  **Add Attributes**: Define configuration parameters (like `value`) as methods within the annotation interface.
4.  **Create an Aspect**:
    *   Create a class annotated with `@Aspect` and `@Component`.
    *   This class will contain the logic triggered by your annotation.
5.  **Define a Pointcut**:
    *   Use expressions like `@annotation(myAnnotation)` to target specific methods.
6.  **Implement Advice**:
    *   `@Before`: Run logic before the method starts.
    *   `@After`: Run logic after the method finishes (successfully or not).
    *   `@Around`: Most powerful; can control execution, modify arguments, and change results.
7.  **Apply and Test**: Annotate your code and verify the behavior (e.g., check logs).

---

## 1. Prerequisites

To use AOP, ensure the following dependency is in your `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

---

## 2. Simple Console Print Annotation (`@Print`)

### Annotation Definition
A simple annotation that prints a message to `System.out.println`.

```java
package com.test.demo.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Print {
    String value() default "Method Executed";
}
```

### Print Aspect
Uses `@Before` advice to intercept method execution.

```java
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
```

## 3. Custom Validation Annotation (`@ValidPan`)

This annotation uses the Bean Validation (JSR 380) API to validate Indian PAN card numbers.

### Dependency
Ensure `spring-boot-starter-validation` is in your `pom.xml`.

### Annotation Definition
```java
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PanValidator.class)
@Documented
public @interface ValidPan {
    String message() default "Invalid PAN format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

### Validator Implementation
```java
public class PanValidator implements ConstraintValidator<ValidPan, String> {
    private static final String PAN_PATTERN = "[A-Z]{5}[0-9]{4}[A-Z]{1}";

    @Override
    public boolean isValid(String pan, ConstraintValidatorContext context) {
        return pan != null && Pattern.matches(PAN_PATTERN, pan);
    }
}
```

### Usage in DTO
```java
public class UserDto {
    @ValidPan
    private String panCard;
    // getters/setters
}
```

---

## 4. Usage in Controller

```java
@RestController
public class TestController {

    @GetMapping("/print")
    @Print("SysOut: Print Annotation Triggered!")
    public String print() {
        return "Check console for output";
    }

    @PostMapping("/user")
    public String createUser(@Valid @RequestBody UserDto userDto) {
        return "User created with PAN: " + userDto.getPanCard();
    }
}
```

---

## 5. Alternative: AOP-based Validation

While Bean Validation is standard, you can also use AOP to intercept method arguments and perform custom checks.

### AOP Aspect Implementation
```java
@Aspect
@Component
public class PanValidationAspect {
    private static final String PAN_PATTERN = "[A-Z]{5}[0-9]{4}[A-Z]{1}";

    @Before("execution(* com.test.demo.controller..*(..)) && args(userDto,..)")
    public void validatePan(UserDto userDto) {
        if (userDto.getPanCard() == null || !Pattern.matches(PAN_PATTERN, userDto.getPanCard())) {
            throw new IllegalArgumentException("AOP Validation Failed!");
        }
    }
}
```

## Summary of AOP Concepts
- **Aspect**: A modularization of a concern that cuts across multiple classes (e.g., `PrintAspect`).
- **Join Point**: A point during the execution of a program, such as the execution of a method.
- **Advice**: Action taken by an aspect at a particular join point (`@Before`, `@Around`, etc.).
- **Pointcut**: A predicate that matches join points (e.g., `@annotation(Print)`).

---

### Note: Validation vs AOP
While AOP can be used for validation, Spring developers prefer **Bean Validation (JSR 380)** for data integrity because:
- **Automatic HTTP mapping**: Spring MVC converts validation failures into standard `400 Bad Request` responses.
- **Field-level focus**: Validation is usually tied to object state (fields), while AOP is tied to method execution.
- **Declarative**: It allows for cleaner DTOs using standard annotations.
