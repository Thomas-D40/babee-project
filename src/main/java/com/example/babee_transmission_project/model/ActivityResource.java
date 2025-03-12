package com.example.babee_transmission_project.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ActivityResource extends BaseResource {
    private String name;
    private String commentaire;
}
