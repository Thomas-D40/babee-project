package com.example.babee_transmission_project.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = HealthActValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HealthAct {
    String message() default "Les données ne sont pas cohérnetes";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}