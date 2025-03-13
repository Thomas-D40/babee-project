package com.example.babee_transmission_project.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.sql.Time;

@Data
@SuperBuilder
public class HealthActResource extends BaseResource {
    private Integer healthActType;
    private Integer temperature;
    private String medecine;
    private String dosage;
    private Time actHour;
}
