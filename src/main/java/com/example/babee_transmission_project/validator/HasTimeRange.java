package com.example.babee_transmission_project.validator;

import java.sql.Time;

public interface HasTimeRange {
    Time getBeginHour();

    Time getEndHour();
}
