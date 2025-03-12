package com.example.babee_transmission_project.repository;

import com.example.babee_transmission_project.entity.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity, UUID>, JpaSpecificationExecutor<ActivityEntity> {
}
