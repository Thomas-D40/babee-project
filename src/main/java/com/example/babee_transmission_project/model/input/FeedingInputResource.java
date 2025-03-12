package com.example.babee_transmission_project.model.input;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class FeedingInputResource {
    @NotNull
    private UUID babeeId;
    @NotNull
    private LocalDate eventDate;
    private List<String> feedingInformations;
}
