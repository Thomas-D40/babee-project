package com.example.babee_transmission_project.repository;

import com.example.babee_transmission_project.entity.SleepingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SleepingRepository extends JpaRepository<SleepingEntity, UUID>, JpaSpecificationExecutor<SleepingEntity> {
}
