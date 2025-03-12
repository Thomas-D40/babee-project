package com.example.babee_transmission_project.converter;

import com.example.babee_transmission_project.entity.InformationEntity;
import com.example.babee_transmission_project.model.InformationResource;
import com.example.babee_transmission_project.model.input.InformationInputResource;

public class InformationConverter {

    public static InformationResource convertEntityToResource(InformationEntity informationEntity) {
        return InformationResource.builder()
                .id(informationEntity.getId())
                .babeeId(informationEntity.getBabeeId())
                .eventDate(informationEntity.getEventDate())
                .commentaire(informationEntity.getCommentaire())
                .build();
    }

    public static InformationEntity convertInputResourceToEntity(InformationInputResource informationInputResource) {
        return InformationEntity.builder()
                .babeeId(informationInputResource.getBabeeId())
                .eventDate(informationInputResource.getEventDate())
                .commentaire(informationInputResource.getCommentaire())
                .build();
    }
}
