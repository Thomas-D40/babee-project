package com.example.babee_transmission_project.repository;

import com.example.babee_transmission_project.entity.FeedingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FeedingRepository extends JpaRepository<FeedingEntity, UUID>, JpaSpecificationExecutor<FeedingEntity> {
}
