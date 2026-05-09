package com.test.demo.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    String level() default "INFO";
    boolean includeArgs() default false;
    boolean includeResult() default false;
}
