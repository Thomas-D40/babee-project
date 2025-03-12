package com.example.babee_transmission_project.service;

import com.example.babee_transmission_project.converter.HealthActConverter;
import com.example.babee_transmission_project.entity.HealthActEntity;
import com.example.babee_transmission_project.exception.NotFoundException;
import com.example.babee_transmission_project.model.HealthActResource;
import com.example.babee_transmission_project.model.input.HealthActInputResource;
import com.example.babee_transmission_project.repository.BabeeRepository;
import com.example.babee_transmission_project.repository.HealthActRepository;
import com.example.babee_transmission_project.specification.HealthActSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class HealthActService {

    private final String MISSING_ERROR = "Pas d'acte de santé avec l'id ";
    private final String MISSING_BABEE_ERROR = "BabeeId incorrect";

    private final HealthActRepository healthActRepository;
    private final BabeeRepository babeeRepository;

    public HealthActService(HealthActRepository healthActRepository, BabeeRepository babeeRepository) {
        this.healthActRepository = healthActRepository;
        this.babeeRepository = babeeRepository;
    }


    public List<HealthActResource> getHealthActs(UUID babeeId, LocalDate day) {
        HealthActSpecification healthActSpecification = new HealthActSpecification();

        Specification<HealthActEntity> filters = Specification.where(Objects.isNull(babeeId) ? null : healthActSpecification.sameBabeeId(babeeId))
                .and(Objects.isNull(day) ? null : healthActSpecification.sameDay(day));

        return healthActRepository.findAll(filters)
                .stream().map(HealthActConverter::convertEntityToResource)
                .toList();
    }

    public HealthActResource getHealthActById(UUID id) throws NotFoundException {
        return healthActRepository.findById(id)
                .map(HealthActConverter::convertEntityToResource)
                .orElseThrow(() -> new NotFoundException(MISSING_ERROR + id));
    }

    public HealthActResource saveHealthAct(HealthActInputResource healthActInputResource) {
        if (babeeRepository.findById(healthActInputResource.getBabeeId()).isEmpty()) {
            throw new NotFoundException(MISSING_BABEE_ERROR);
        }

        HealthActEntity healthActEntity = HealthActConverter.convertInputResourceToEntity(healthActInputResource);

        return HealthActConverter.convertEntityToResource(healthActRepository.saveAndFlush(healthActEntity));
    }

    public HealthActResource updateHealthAct(UUID id, HealthActInputResource healthActInputResource) {
        if (healthActRepository.findById(id).isEmpty()) {
            throw new NotFoundException(MISSING_ERROR + id);
        }

        if (babeeRepository.findById(healthActInputResource.getBabeeId()).isEmpty()) {
            throw new NotFoundException(MISSING_BABEE_ERROR);
        }

        HealthActEntity updatedHealthActEntity = HealthActConverter.convertInputResourceToEntity(healthActInputResource);
        updatedHealthActEntity.setId(id);

        return HealthActConverter.convertEntityToResource(healthActRepository.saveAndFlush(updatedHealthActEntity));
    }

    public void deleteHealthActById(UUID id) {
        Optional<HealthActEntity> maybeHealthActEntity = healthActRepository.findById(id);

        if (maybeHealthActEntity.isEmpty()) {
            throw new NotFoundException(MISSING_ERROR + id);
        }

        HealthActEntity healthActEntity = maybeHealthActEntity.get();

        healthActRepository.delete(healthActEntity);
    }
}
