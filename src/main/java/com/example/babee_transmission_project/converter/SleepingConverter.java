package com.example.babee_transmission_project.converter;

import com.example.babee_transmission_project.entity.SleepingEntity;
import com.example.babee_transmission_project.model.SleepingResource;
import com.example.babee_transmission_project.model.input.SleepingInputResource;

public class SleepingConverter {

    public static SleepingResource convertEntityToResource(SleepingEntity sleepingEntity) {
        return SleepingResource.builder()
                .id(sleepingEntity.getId())
                .babeeId(sleepingEntity.getBabeeId())
                .eventDate(sleepingEntity.getEventDate())
                .debut(sleepingEntity.getDebut())
                .fin(sleepingEntity.getFin())
                .build();
    }

    public static SleepingEntity convertInputResourceToEntity(SleepingInputResource sleepingInputResource) {
        return SleepingEntity.builder()
                .babeeId(sleepingInputResource.getBabeeId())
                .eventDate(sleepingInputResource.getEventDate())
                .debut(sleepingInputResource.getDebut())
                .fin(sleepingInputResource.getFin())
                .build();
    }
}
