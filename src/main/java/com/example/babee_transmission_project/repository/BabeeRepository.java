package com.example.babee_transmission_project.repository;

import com.example.babee_transmission_project.entity.BabeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BabeeRepository extends JpaRepository<BabeeEntity, UUID> {
}
