package com.example.babee_transmission_project.service;

import com.example.babee_transmission_project.converter.CareActConverter;
import com.example.babee_transmission_project.entity.CareActEntity;
import com.example.babee_transmission_project.exception.NotFoundException;
import com.example.babee_transmission_project.model.CareActResource;
import com.example.babee_transmission_project.model.input.CareActInputResource;
import com.example.babee_transmission_project.repository.BabeeRepository;
import com.example.babee_transmission_project.repository.CareActRepository;
import com.example.babee_transmission_project.specification.CareActSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class CareActService {

    private final String MISSING_ERROR = "Pas d'acte de soin avec l'id ";
    private final String MISSING_BABEE_ERROR = "BabeeId incorrect";

    private final CareActRepository careActRepository;
    private final BabeeRepository babeeRepository;

    public CareActService(CareActRepository careActRepository, BabeeRepository babeeRepository) {
        this.careActRepository = careActRepository;
        this.babeeRepository = babeeRepository;
    }


    public List<CareActResource> getCareActs(UUID babeeId, LocalDate day) {
        CareActSpecification careActSpecification = new CareActSpecification();

        Specification<CareActEntity> filters = Specification.where(Objects.isNull(babeeId) ? null : careActSpecification.sameBabeeId(babeeId))
                .and(Objects.isNull(day) ? null : careActSpecification.sameDay(day));


        return careActRepository.findAll(filters)
                .stream().map(CareActConverter::convertEntityToResource)
                .toList();
    }

    public CareActResource getCareActById(UUID id) throws NotFoundException {
        return careActRepository.findById(id)
                .map(CareActConverter::convertEntityToResource)
                .orElseThrow(() -> new NotFoundException(MISSING_ERROR + id));
    }

    public CareActResource saveCareAct(CareActInputResource careActInputResource) {
        if (babeeRepository.findById(careActInputResource.getBabeeId()).isEmpty()) {
            throw new NotFoundException(MISSING_BABEE_ERROR);
        }

        CareActEntity careActEntity = CareActConverter.convertInputResourceToEntity(careActInputResource);

        return CareActConverter.convertEntityToResource(careActRepository.saveAndFlush(careActEntity));
    }

    public CareActResource updateCareAct(UUID id, CareActInputResource careActInputResource) {
        if (careActRepository.findById(id).isEmpty()) {
            throw new NotFoundException(MISSING_ERROR + id);
        }

        if (babeeRepository.findById(careActInputResource.getBabeeId()).isEmpty()) {
            throw new NotFoundException(MISSING_BABEE_ERROR);
        }

        CareActEntity updatedCareActEntity = CareActConverter.convertInputResourceToEntity(careActInputResource);
        updatedCareActEntity.setId(id);

        return CareActConverter.convertEntityToResource(careActRepository.saveAndFlush(updatedCareActEntity));
    }

    public void deleteCareActById(UUID id) {
        Optional<CareActEntity> maybeCareActEntity = careActRepository.findById(id);

        if (maybeCareActEntity.isEmpty()) {
            throw new NotFoundException(MISSING_ERROR + id);
        }

        CareActEntity careActEntity = maybeCareActEntity.get();

        careActRepository.delete(careActEntity);
    }
}
