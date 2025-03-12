package com.example.babee_transmission_project.converter;

import com.example.babee_transmission_project.entity.FeedingEntity;
import com.example.babee_transmission_project.model.FeedingResource;
import com.example.babee_transmission_project.model.input.FeedingInputResource;

public class FeedingConverter {

    public static FeedingResource convertEntityToResource(FeedingEntity feedingEntity) {
        return FeedingResource.builder()
                .id(feedingEntity.getId())
                .babeeId(feedingEntity.getBabeeId())
                .eventDate(feedingEntity.getEventDate())
                .feedingInformations(feedingEntity.getFeedingInformations())
                .build();
    }

    public static FeedingEntity convertInputResourceToEntity(FeedingInputResource feedingInputResource) {
        return FeedingEntity.builder()
                .babeeId(feedingInputResource.getBabeeId())
                .eventDate(feedingInputResource.getEventDate())
                .feedingInformations(feedingInputResource.getFeedingInformations())
                .build();
    }
}
