package com.hipradeep.code.jpa;

import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation for Order ID generation using OrderIdGenerator.
 * Replaces deprecated @GenericGenerator strategy.
 */
@IdGeneratorType(OrderIdGenerator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface OrderIdGeneration {
    String prefix() default "ODER_";
}
