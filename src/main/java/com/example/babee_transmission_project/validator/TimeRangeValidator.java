package com.example.babee_transmission_project.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.sql.Time;
import java.util.Objects;

public class TimeRangeValidator implements ConstraintValidator<ValidTime, HasTimeRange> {
    @Override
    public void initialize(ValidTime constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(HasTimeRange object, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(object)) {
            return true;
        }

        Time debut = object.getBeginHour();
        Time fin = object.getEndHour();

        if (Objects.isNull(debut)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Ne peut être null.")
                    .addPropertyNode("beginHour")
                    .addConstraintViolation();

            return false;
        }

        if (Objects.isNull(fin)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Ne peut être null.")
                    .addPropertyNode("endHour")
                    .addConstraintViolation();

            return false;
        }

        if (debut.after(fin)) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Les temps ne sont pas cohérents")
                    .addPropertyNode("beginHour")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}
