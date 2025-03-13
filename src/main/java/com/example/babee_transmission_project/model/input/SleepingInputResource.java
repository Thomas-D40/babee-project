package com.example.babee_transmission_project.model.input;

import com.example.babee_transmission_project.validator.HasTimeRange;
import com.example.babee_transmission_project.validator.ValidTime;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@ValidTime
public class SleepingInputResource implements HasTimeRange {
    @NotNull
    private UUID babeeId;
    @NotNull
    private LocalDate eventDate;
    private Time beginHour;
    private Time endHour;
}
