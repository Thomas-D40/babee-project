package com.example.babee_transmission_project.service;

import com.example.babee_transmission_project.converter.ActivityConverter;
import com.example.babee_transmission_project.entity.ActivityEntity;
import com.example.babee_transmission_project.exception.NotFoundException;
import com.example.babee_transmission_project.model.ActivityResource;
import com.example.babee_transmission_project.model.input.ActivityInputResource;
import com.example.babee_transmission_project.repository.ActivityRepository;
import com.example.babee_transmission_project.repository.BabeeRepository;
import com.example.babee_transmission_project.specification.ActivitySpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ActivityService {

    private final String MISSING_ERROR = "Pas d'activité avec l'id ";
    private final String MISSING_BABEE_ERROR = "BabeeId incorrect";

    private final ActivityRepository activityRepository;
    private final BabeeRepository babeeRepository;

    public ActivityService(ActivityRepository activityRepository, BabeeRepository babeeRepository) {
        this.activityRepository = activityRepository;
        this.babeeRepository = babeeRepository;
    }


    public List<ActivityResource> getActivities(UUID babeeId, LocalDate day) {
        ActivitySpecification activitySpecification = new ActivitySpecification();
        
        Specification<ActivityEntity> filters = Specification.where(Objects.isNull(babeeId) ? null : activitySpecification.sameBabeeId(babeeId))
                .and(Objects.isNull(day) ? null : activitySpecification.sameDay(day));

        return activityRepository.findAll(filters)
                .stream().map(ActivityConverter::convertEntityToResource)
                .toList();
    }

    public ActivityResource getActivityById(UUID id) throws NotFoundException {
        return activityRepository.findById(id)
                .map(ActivityConverter::convertEntityToResource)
                .orElseThrow(() -> new NotFoundException(MISSING_ERROR + id));
    }

    public ActivityResource saveActivity(ActivityInputResource activityInputResource) {
        if (babeeRepository.findById(activityInputResource.getBabeeId()).isEmpty()) {
            throw new NotFoundException(MISSING_BABEE_ERROR);
        }

        ActivityEntity activityEntity = ActivityConverter.convertInputResourceToEntity(activityInputResource);

        return ActivityConverter.convertEntityToResource(activityRepository.saveAndFlush(activityEntity));
    }

    public ActivityResource updateActivity(UUID id, ActivityInputResource activityInputResource) {
        if (activityRepository.findById(id).isEmpty()) {
            throw new NotFoundException(MISSING_ERROR + id);
        }

        if (babeeRepository.findById(activityInputResource.getBabeeId()).isEmpty()) {
            throw new NotFoundException(MISSING_BABEE_ERROR);
        }

        ActivityEntity updatedActivityEntity = ActivityConverter.convertInputResourceToEntity(activityInputResource);
        updatedActivityEntity.setId(id);

        return ActivityConverter.convertEntityToResource(activityRepository.saveAndFlush(updatedActivityEntity));
    }

    public void deleteActivityById(UUID id) {
        Optional<ActivityEntity> maybeActivityEntity = activityRepository.findById(id);

        if (maybeActivityEntity.isEmpty()) {
            throw new NotFoundException(MISSING_ERROR + id);
        }

        ActivityEntity activityEntity = maybeActivityEntity.get();

        activityRepository.delete(activityEntity);
    }
}
