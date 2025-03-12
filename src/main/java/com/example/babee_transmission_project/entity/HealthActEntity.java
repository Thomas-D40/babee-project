package com.example.babee_transmission_project.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@SuperBuilder
@Getter
@Setter
@Table(name = "healthact")
@AllArgsConstructor
@NoArgsConstructor
public class HealthActEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private Integer healthActType;
    private Integer temperature;
    private String nomMedicament;
    private String dosage;
}
