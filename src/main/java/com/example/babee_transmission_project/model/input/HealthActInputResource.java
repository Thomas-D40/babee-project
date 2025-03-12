package com.example.babee_transmission_project.model.input;

import com.example.babee_transmission_project.validator.HealthAct;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@HealthAct
public class HealthActInputResource {
    @NotNull
    private UUID babeeId;
    @NotNull
    private LocalDate eventDate;
    @NotNull
    private Integer healthActType;
    private Integer temperature;
    private String nomMedicament;
    private String dosage;
}
