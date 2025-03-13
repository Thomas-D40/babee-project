package com.example.babee_transmission_project.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.sql.Time;

@Data
@SuperBuilder
public class SleepingResource extends BaseResource {
    private Time beginHour;
    private Time endHour;
}
