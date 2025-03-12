package com.example.babee_transmission_project.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class CareActResource extends BaseResource {
    private Integer careActType;
    private Integer careActDetail;
    private String commentaire;
}
