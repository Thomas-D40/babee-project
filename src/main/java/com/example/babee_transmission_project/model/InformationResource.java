package com.example.babee_transmission_project.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class InformationResource extends BaseResource {
    private String commentaire;
}
