package com.example.babee_transmission_project.converter;

import com.example.babee_transmission_project.entity.BabeeEntity;
import com.example.babee_transmission_project.model.BabeeResource;
import com.example.babee_transmission_project.model.input.BabeeInputResource;

public class BabeeConverter {

    public static BabeeResource convertEntityToResource(BabeeEntity babeeEntity) {
        return BabeeResource.builder()
                .id(babeeEntity.getId())
                .prenom(babeeEntity.getPrenom())
                .nom(babeeEntity.getNom())
                .dateNaissance(babeeEntity.getDateNaissance())
                .build();
    }

    public static BabeeEntity convertInputResourceToEntity(BabeeInputResource babeeInputResource) {
        return BabeeEntity.builder()
                .prenom(babeeInputResource.getPrenom())
                .nom(babeeInputResource.getNom())
                .dateNaissance(babeeInputResource.getDateNaissance())
                .build();
    }
}
