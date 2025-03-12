package com.example.babee_transmission_project.service;

import com.example.babee_transmission_project.converter.InformationConverter;
import com.example.babee_transmission_project.entity.InformationEntity;
import com.example.babee_transmission_project.exception.NotFoundException;
import com.example.babee_transmission_project.model.InformationResource;
import com.example.babee_transmission_project.model.input.InformationInputResource;
import com.example.babee_transmission_project.repository.BabeeRepository;
import com.example.babee_transmission_project.repository.InformationRepository;
import com.example.babee_transmission_project.specification.InformationSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class InformationService {

    private final String MISSING_ERROR = "Pas d'information avec l'id ";
    private final String MISSING_BABEE_ERROR = "BabeeId incorrect";

    private final InformationRepository informationRepository;
    private final BabeeRepository babeeRepository;

    public InformationService(InformationRepository informationRepository, BabeeRepository babeeRepository) {
        this.informationRepository = informationRepository;
        this.babeeRepository = babeeRepository;
    }


    public List<InformationResource> getInformations(UUID babeeId, LocalDate day) {
        InformationSpecification informationSpecification = new InformationSpecification();

        Specification<InformationEntity> filters = Specification.where(Objects.isNull(babeeId) ? null : informationSpecification.sameBabeeId(babeeId))
                .and(Objects.isNull(day) ? null : informationSpecification.sameDay(day));

        return informationRepository.findAll(filters)
                .stream().map(InformationConverter::convertEntityToResource)
                .toList();
    }

    public InformationResource getInformationById(UUID id) throws NotFoundException {
        return informationRepository.findById(id)
                .map(InformationConverter::convertEntityToResource)
                .orElseThrow(() -> new NotFoundException(MISSING_ERROR + id));
    }

    public InformationResource saveInformation(InformationInputResource informationInputResource) {
        if (babeeRepository.findById(informationInputResource.getBabeeId()).isEmpty()) {
            throw new NotFoundException(MISSING_BABEE_ERROR);
        }

        InformationEntity informationEntity = InformationConverter.convertInputResourceToEntity(informationInputResource);

        return InformationConverter.convertEntityToResource(informationRepository.saveAndFlush(informationEntity));
    }

    public InformationResource updateInformation(UUID id, InformationInputResource informationInputResource) {
        if (informationRepository.findById(id).isEmpty()) {
            throw new NotFoundException(MISSING_ERROR + id);
        }

        if (babeeRepository.findById(informationInputResource.getBabeeId()).isEmpty()) {
            throw new NotFoundException(MISSING_BABEE_ERROR);
        }

        InformationEntity updatedInformationEntity = InformationConverter.convertInputResourceToEntity(informationInputResource);
        updatedInformationEntity.setId(id);

        return InformationConverter.convertEntityToResource(informationRepository.saveAndFlush(updatedInformationEntity));
    }

    public void deleteInformationById(UUID id) {
        Optional<InformationEntity> maybeInformationEntity = informationRepository.findById(id);

        if (maybeInformationEntity.isEmpty()) {
            throw new NotFoundException(MISSING_ERROR + id);
        }

        InformationEntity informationEntity = maybeInformationEntity.get();

        informationRepository.delete(informationEntity);
    }
}
