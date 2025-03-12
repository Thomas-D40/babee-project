package com.example.babee_transmission_project.service;

import com.example.babee_transmission_project.converter.SleepingConverter;
import com.example.babee_transmission_project.entity.SleepingEntity;
import com.example.babee_transmission_project.exception.NotFoundException;
import com.example.babee_transmission_project.model.SleepingResource;
import com.example.babee_transmission_project.model.input.SleepingInputResource;
import com.example.babee_transmission_project.repository.BabeeRepository;
import com.example.babee_transmission_project.repository.SleepingRepository;
import com.example.babee_transmission_project.specification.SleepingSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class SleepingService {

    private final String MISSING_ERROR = "Pas de sommeil avec l'id ";
    private final String MISSING_BABEE_ERROR = "BabeeId incorrect";

    private final SleepingRepository sleepingRepository;
    private final BabeeRepository babeeRepository;

    public SleepingService(SleepingRepository sleepingRepository, BabeeRepository babeeRepository) {
        this.sleepingRepository = sleepingRepository;
        this.babeeRepository = babeeRepository;
    }


    public List<SleepingResource> getSleepings(UUID babeeId, LocalDate day) {
        SleepingSpecification sleepingSpecification = new SleepingSpecification();

        Specification<SleepingEntity> filters = Specification.where(Objects.isNull(babeeId) ? null : sleepingSpecification.sameBabeeId(babeeId))
                .and(Objects.isNull(day) ? null : sleepingSpecification.sameDay(day));

        return sleepingRepository.findAll(filters)
                .stream().map(SleepingConverter::convertEntityToResource)
                .toList();
    }

    public SleepingResource getSleepingById(UUID id) throws NotFoundException {
        return sleepingRepository.findById(id)
                .map(SleepingConverter::convertEntityToResource)
                .orElseThrow(() -> new NotFoundException(MISSING_ERROR + id));
    }

    public SleepingResource saveSleeping(SleepingInputResource sleepingInputResource) {
        if (babeeRepository.findById(sleepingInputResource.getBabeeId()).isEmpty()) {
            throw new NotFoundException(MISSING_BABEE_ERROR);
        }

        SleepingEntity sleepingEntity = SleepingConverter.convertInputResourceToEntity(sleepingInputResource);

        return SleepingConverter.convertEntityToResource(sleepingRepository.saveAndFlush(sleepingEntity));
    }

    public SleepingResource updateSleeping(UUID id, SleepingInputResource sleepingInputResource) {
        if (sleepingRepository.findById(id).isEmpty()) {
            throw new NotFoundException(MISSING_ERROR + id);
        }

        if (babeeRepository.findById(sleepingInputResource.getBabeeId()).isEmpty()) {
            throw new NotFoundException(MISSING_BABEE_ERROR);
        }

        SleepingEntity updatedSleepingEntity = SleepingConverter.convertInputResourceToEntity(sleepingInputResource);
        updatedSleepingEntity.setId(id);

        return SleepingConverter.convertEntityToResource(sleepingRepository.saveAndFlush(updatedSleepingEntity));
    }

    public void deleteSleepingById(UUID id) {
        Optional<SleepingEntity> maybeSleepingEntity = sleepingRepository.findById(id);

        if (maybeSleepingEntity.isEmpty()) {
            throw new NotFoundException(MISSING_ERROR + id);
        }

        SleepingEntity sleepingEntity = maybeSleepingEntity.get();

        sleepingRepository.delete(sleepingEntity);
    }
}
