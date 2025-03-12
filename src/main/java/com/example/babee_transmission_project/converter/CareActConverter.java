package com.example.babee_transmission_project.converter;

import com.example.babee_transmission_project.entity.CareActEntity;
import com.example.babee_transmission_project.model.CareActResource;
import com.example.babee_transmission_project.model.input.CareActInputResource;

public class CareActConverter {

    public static CareActResource convertEntityToResource(CareActEntity careActEntity) {
        return CareActResource.builder()
                .id(careActEntity.getId())
                .babeeId(careActEntity.getBabeeId())
                .eventDate(careActEntity.getEventDate())
                .careActType(careActEntity.getCareActType())
                .careActDetail(careActEntity.getCareActDetail())
                .commentaire(careActEntity.getCommentaire())
                .build();
    }

    public static CareActEntity convertInputResourceToEntity(CareActInputResource careActInputResource) {
        return CareActEntity.builder()
                .babeeId(careActInputResource.getBabeeId())
                .eventDate(careActInputResource.getEventDate())
                .careActType(careActInputResource.getCareActType())
                .careActDetail(careActInputResource.getCareActDetail())
                .commentaire(careActInputResource.getCommentaire())
                .build();
    }
}
