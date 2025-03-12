package com.example.babee_transmission_project.converter;

import com.example.babee_transmission_project.entity.HealthActEntity;
import com.example.babee_transmission_project.model.HealthActResource;
import com.example.babee_transmission_project.model.input.HealthActInputResource;

public class HealthActConverter {

    public static HealthActResource convertEntityToResource(HealthActEntity healthActEntity) {
        return HealthActResource.builder()
                .id(healthActEntity.getId())
                .babeeId(healthActEntity.getBabeeId())
                .eventDate(healthActEntity.getEventDate())
                .healthActType(healthActEntity.getHealthActType())
                .temperature(healthActEntity.getTemperature())
                .nomMedicament(healthActEntity.getNomMedicament())
                .dosage(healthActEntity.getDosage())
                .build();
    }

    public static HealthActEntity convertInputResourceToEntity(HealthActInputResource healthActInputResource) {
        return HealthActEntity.builder()
                .babeeId(healthActInputResource.getBabeeId())
                .eventDate(healthActInputResource.getEventDate())
                .healthActType(healthActInputResource.getHealthActType())
                .temperature(healthActInputResource.getTemperature())
                .nomMedicament(healthActInputResource.getNomMedicament())
                .dosage(healthActInputResource.getDosage())
                .build();
    }
}
