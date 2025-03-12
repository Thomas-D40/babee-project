package com.example.babee_transmission_project.service;

import com.example.babee_transmission_project.converter.FeedingConverter;
import com.example.babee_transmission_project.entity.FeedingEntity;
import com.example.babee_transmission_project.exception.NotFoundException;
import com.example.babee_transmission_project.model.FeedingResource;
import com.example.babee_transmission_project.model.input.FeedingInputResource;
import com.example.babee_transmission_project.repository.BabeeRepository;
import com.example.babee_transmission_project.repository.FeedingRepository;
import com.example.babee_transmission_project.specification.FeedingSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class FeedingService {

    private final String MISSING_ERROR = "Pas de repas avec l'id ";
    private final String MISSING_BABEE_ERROR = "BabeeId incorrect";

    private final FeedingRepository feedingRepository;
    private final BabeeRepository babeeRepository;

    public FeedingService(FeedingRepository feedingRepository, BabeeRepository babeeRepository) {
        this.feedingRepository = feedingRepository;
        this.babeeRepository = babeeRepository;
    }


    public List<FeedingResource> getFeedings(UUID babeeId, LocalDate day) {
        FeedingSpecification feedingSpecification = new FeedingSpecification();

        Specification<FeedingEntity> filters = Specification.where(Objects.isNull(babeeId) ? null : feedingSpecification.sameBabeeId(babeeId))
                .and(Objects.isNull(day) ? null : feedingSpecification.sameDay(day));

        return feedingRepository.findAll(filters)
                .stream().map(FeedingConverter::convertEntityToResource)
                .toList();
    }

    public FeedingResource getFeedingById(UUID id) throws NotFoundException {
        return feedingRepository.findById(id)
                .map(FeedingConverter::convertEntityToResource)
                .orElseThrow(() -> new NotFoundException(MISSING_ERROR + id));
    }

    public FeedingResource saveFeeding(FeedingInputResource feedingInputResource) {
        if (babeeRepository.findById(feedingInputResource.getBabeeId()).isEmpty()) {
            throw new NotFoundException(MISSING_BABEE_ERROR);
        }

        FeedingEntity feedingEntity = FeedingConverter.convertInputResourceToEntity(feedingInputResource);

        return FeedingConverter.convertEntityToResource(feedingRepository.saveAndFlush(feedingEntity));
    }

    public FeedingResource updateFeeding(UUID id, FeedingInputResource feedingInputResource) {
        if (feedingRepository.findById(id).isEmpty()) {
            throw new NotFoundException(MISSING_ERROR + id);
        }

        if (babeeRepository.findById(feedingInputResource.getBabeeId()).isEmpty()) {
            throw new NotFoundException(MISSING_BABEE_ERROR);
        }

        FeedingEntity updatedFeedingEntity = FeedingConverter.convertInputResourceToEntity(feedingInputResource);
        updatedFeedingEntity.setId(id);

        return FeedingConverter.convertEntityToResource(feedingRepository.saveAndFlush(updatedFeedingEntity));
    }

    public void deleteFeedingById(UUID id) {
        Optional<FeedingEntity> maybeFeedingEntity = feedingRepository.findById(id);

        if (maybeFeedingEntity.isEmpty()) {
            throw new NotFoundException(MISSING_ERROR + id);
        }

        FeedingEntity feedingEntity = maybeFeedingEntity.get();

        feedingRepository.delete(feedingEntity);
    }
}
