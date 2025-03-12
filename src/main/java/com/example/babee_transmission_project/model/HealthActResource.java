package com.example.babee_transmission_project.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class HealthActResource extends BaseResource {
    private Integer healthActType;
    private Integer temperature;
    private String nomMedicament;
    private String dosage;
}
