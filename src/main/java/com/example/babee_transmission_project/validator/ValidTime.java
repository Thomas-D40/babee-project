package com.example.babee_transmission_project.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TimeRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTime {
    String message() default "Les temps ne sont pas cohérents";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}