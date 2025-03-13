package com.example.babee_transmission_project.converter;

import com.example.babee_transmission_project.entity.BabeeEntity;
import com.example.babee_transmission_project.model.BabeeResource;
import com.example.babee_transmission_project.model.input.BabeeInputResource;

public class BabeeConverter {

    public static BabeeResource convertEntityToResource(BabeeEntity babeeEntity) {
        return BabeeResource.builder()
                .id(babeeEntity.getId())
                .firstName(babeeEntity.getFirstName())
                .lastName(babeeEntity.getLastName())
                .birthDate(babeeEntity.getBirthDate())
                .build();
    }

    public static BabeeEntity convertInputResourceToEntity(BabeeInputResource babeeInputResource) {
        return BabeeEntity.builder()
                .firstName(babeeInputResource.getFirstName())
                .lastName(babeeInputResource.getLastName())
                .birthDate(babeeInputResource.getBirthDate())
                .build();
    }
}
