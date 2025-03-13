package com.example.babee_transmission_project.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.Time;
import java.util.UUID;

@Entity
@SuperBuilder
@Getter
@Setter
@Table(name = "sleeping")
@AllArgsConstructor
@NoArgsConstructor
public class SleepingEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private Time beginHour;
    private Time endHour;
}
