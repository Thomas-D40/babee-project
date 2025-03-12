package com.example.babee_transmission_project.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Data
@SuperBuilder
public abstract class BaseResource {
    private UUID id;
    private UUID babeeId;
    private LocalDate eventDate;
}
