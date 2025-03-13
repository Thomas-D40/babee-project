package com.example.babee_transmission_project.converter;

import com.example.babee_transmission_project.entity.ActivityEntity;
import com.example.babee_transmission_project.model.ActivityResource;
import com.example.babee_transmission_project.model.input.ActivityInputResource;

public class ActivityConverter {

    public static ActivityResource convertEntityToResource(ActivityEntity activityEntity) {
        return ActivityResource.builder()
                .id(activityEntity.getId())
                .babeeId(activityEntity.getBabeeId())
                .name(activityEntity.getName())
                .comment(activityEntity.getComment())
                .eventDate(activityEntity.getEventDate())
                .build();
    }

    public static ActivityEntity convertInputResourceToEntity(ActivityInputResource activityInputResource) {
        return ActivityEntity.builder()
                .babeeId(activityInputResource.getBabeeId())
                .name(activityInputResource.getName())
                .comment(activityInputResource.getComment())
                .eventDate(activityInputResource.getEventDate())
                .build();
    }
}
