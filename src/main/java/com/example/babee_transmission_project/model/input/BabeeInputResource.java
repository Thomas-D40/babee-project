package com.example.babee_transmission_project.model.input;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BabeeInputResource {
    @NotNull
    private String nom;
    @NotNull
    private String prenom;
    @NotNull
    private LocalDate dateNaissance;
}
