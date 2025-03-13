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
                .medecine(healthActEntity.getMedecine())
                .dosage(healthActEntity.getDosage())
                .actHour(healthActEntity.getActHour())
                .build();
    }

    public static HealthActEntity convertInputResourceToEntity(HealthActInputResource healthActInputResource) {
        return HealthActEntity.builder()
                .babeeId(healthActInputResource.getBabeeId())
                .eventDate(healthActInputResource.getEventDate())
                .healthActType(healthActInputResource.getHealthActType())
                .temperature(healthActInputResource.getTemperature())
                .medecine(healthActInputResource.getMedecine())
                .dosage(healthActInputResource.getDosage())
                .actHour(healthActInputResource.getActHour())
                .build();
    }
}
