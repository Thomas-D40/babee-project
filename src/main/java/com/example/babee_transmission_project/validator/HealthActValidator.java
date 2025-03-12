package com.example.babee_transmission_project.validator;

import com.example.babee_transmission_project.model.input.HealthActInputResource;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class HealthActValidator implements ConstraintValidator<HealthAct, HealthActInputResource> {
    @Override
    public void initialize(HealthAct constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(HealthActInputResource object, ConstraintValidatorContext context) {
        if (Objects.isNull(object)) {
            return true;
        }

        Integer healthActType = object.getHealthActType();
        Integer temperature = object.getTemperature();
        String nomMedicament = object.getNomMedicament();
        String dosage = object.getDosage();

        if (Objects.isNull(healthActType) || (healthActType != 1 && healthActType != 2)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Type d'acte de soin invalide")
                    .addPropertyNode("healthActType")
                    .addConstraintViolation();
            return false;
        }

        if (Objects.nonNull(temperature) && (Objects.nonNull(nomMedicament) || Objects.nonNull(dosage))) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Température & Médicaments ne peuvent être indiqués ensemble.")
                    .addPropertyNode("healthActType")
                    .addConstraintViolation();
            return false;
        }

        if (healthActType == 1 && Objects.isNull(temperature)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("La température doit être indiquée.")
                    .addPropertyNode("temperature")
                    .addConstraintViolation();
            return false;
        }

        if (healthActType == 2 && (Objects.isNull(nomMedicament) || Objects.isNull(dosage))) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Il manque le nom du médicament et/ou le dosage")
                    .addPropertyNode("healthActType")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
