package com.example.babee_transmission_project.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class BabeeResource {
    private UUID id;
    private String lastName;
    private String firstName;
    private LocalDate birthDate;
}
