package com.example.babee_transmission_project.entity;


import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;

@Entity
@SuperBuilder
@Getter
@Setter
@Table(name = "feeding")
@AllArgsConstructor
@NoArgsConstructor
public class FeedingEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> feedingInformations;
}
