package com.example.babee_transmission_project.service;

import com.example.babee_transmission_project.converter.FeedingConverter;
import com.example.babee_transmission_project.entity.FeedingEntity;
import com.example.babee_transmission_project.exception.NotFoundException;
import com.example.babee_transmission_project.model.FeedingResource;
import com.example.babee_transmission_project.model.input.FeedingInputResource;
import com.example.babee_transmission_project.repository.BabeeRepository;
import com.example.babee_transmission_project.repository.FeedingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedingServiceTest {

    private final String MISSING_ERROR = "Pas de repas avec l'id ";
    private final String MISSING_BABEE_ERROR = "BabeeId incorrect";
    @Mock
    private FeedingRepository feedingRepository;
    @Mock
    private BabeeRepository babeeRepository;
    private FeedingService feedingService;
    private UUID babeeId;
    private UUID feedingId;
    private LocalDate eventDate;
    private FeedingEntity feedingEntity;
    private FeedingResource feedingResource;
    private FeedingInputResource feedingInputResource;

    @BeforeEach
    void setUp() {
        feedingService = new FeedingService(feedingRepository, babeeRepository);

        babeeId = UUID.randomUUID();
        feedingId = UUID.randomUUID();
        eventDate = LocalDate.now();

        feedingEntity = FeedingEntity.builder()
                .id(feedingId)
                .babeeId(babeeId)
                .eventDate(eventDate)
                .feedingInformations(List.of("Biberon - 150ml"))
                .build();

        feedingResource = FeedingResource.builder()
                .id(feedingId)
                .babeeId(babeeId)
                .eventDate(eventDate)
                .feedingInformations(List.of("Biberon - 150ml"))
                .build();

        feedingInputResource = FeedingInputResource.builder()
                .babeeId(babeeId)
                .eventDate(eventDate)
                .feedingInformations(List.of("Biberon - 150ml"))
                .build();
    }

    @Test
    void getActivities_ShouldReturnListOfActivities() {
        when(feedingRepository.findAll((Specification<FeedingEntity>) any())).thenReturn(List.of(feedingEntity));

        try (var mocked = Mockito.mockStatic(FeedingConverter.class)) {
            mocked.when(() -> FeedingConverter.convertEntityToResource(feedingEntity))
                    .thenReturn(feedingResource);

            List<FeedingResource> result = feedingService.getFeedings(babeeId, eventDate);

            assertThat(result).isNotNull().hasSize(1);
            assertThat(result.getFirst()).isEqualTo(feedingResource);
        }
    }

    @Test
    void getActivityById_WhenActivityExists_ShouldReturnActivity() {
        when(feedingRepository.findById(feedingId)).thenReturn(Optional.of(feedingEntity));

        try (var mocked = Mockito.mockStatic(FeedingConverter.class)) {
            mocked.when(() -> FeedingConverter.convertEntityToResource(feedingEntity))
                    .thenReturn(feedingResource);

            FeedingResource result = feedingService.getFeedingById(feedingId);

            assertThat(result).isEqualTo(feedingResource);
            verify(feedingRepository).findById(feedingId);
        }
    }

    @Test
    void getActivityById_WhenActivityDoesNotExist_ShouldThrowNotFoundException() {
        when(feedingRepository.findById(feedingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> feedingService.getFeedingById(feedingId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_ERROR + feedingId);
    }

    @Test
    void saveActivity_ShouldSaveAndReturnActivity() {
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.of(mock()));

        try (var mocked = Mockito.mockStatic(FeedingConverter.class)) {
            mocked.when(() -> FeedingConverter.convertInputResourceToEntity(feedingInputResource))
                    .thenReturn(feedingEntity);
            mocked.when(() -> FeedingConverter.convertEntityToResource(feedingEntity))
                    .thenReturn(feedingResource);

            when(feedingRepository.saveAndFlush(feedingEntity)).thenReturn(feedingEntity);

            FeedingResource result = feedingService.saveFeeding(feedingInputResource);

            assertThat(result).isEqualTo(feedingResource);
            verify(feedingRepository).saveAndFlush(feedingEntity);
        }
    }

    @Test
    void saveActivity_WhenBabeeDoesNotExist_ShouldThrowNotFoundException() {
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> feedingService.saveFeeding(feedingInputResource))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_BABEE_ERROR);
    }

    @Test
    void updateActivity_WhenActivityExists_ShouldUpdateAndReturnActivity() {
        when(feedingRepository.findById(feedingId)).thenReturn(Optional.of(feedingEntity));
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.of(mock()));

        try (var mocked = Mockito.mockStatic(FeedingConverter.class)) {
            mocked.when(() -> FeedingConverter.convertInputResourceToEntity(feedingInputResource))
                    .thenReturn(feedingEntity);
            mocked.when(() -> FeedingConverter.convertEntityToResource(feedingEntity))
                    .thenReturn(feedingResource);

            when(feedingRepository.saveAndFlush(feedingEntity)).thenReturn(feedingEntity);

            FeedingResource result = feedingService.updateFeeding(feedingId, feedingInputResource);

            assertThat(result).isEqualTo(feedingResource);
            verify(feedingRepository).saveAndFlush(feedingEntity);
        }
    }

    @Test
    void updateActivity_WhenActivityDoesNotExist_ShouldThrowNotFoundException() {
        when(feedingRepository.findById(feedingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> feedingService.updateFeeding(feedingId, feedingInputResource))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_ERROR + feedingId);
    }

    @Test
    void updateActivity_WhenBabeeDoesNotExist_ShouldThrowNotFoundException() {
        when(feedingRepository.findById(feedingId)).thenReturn(Optional.of(feedingEntity));
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> feedingService.updateFeeding(feedingId, feedingInputResource))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_BABEE_ERROR);
    }

    @Test
    void deleteActivityById_WhenActivityExists_ShouldDeleteActivity() {
        when(feedingRepository.findById(feedingId)).thenReturn(Optional.of(feedingEntity));

        feedingService.deleteFeedingById(feedingId);

        verify(feedingRepository).delete(feedingEntity);
    }

    @Test
    void deleteActivityById_WhenActivityDoesNotExist_ShouldThrowNotFoundException() {
        when(feedingRepository.findById(feedingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> feedingService.deleteFeedingById(feedingId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_ERROR + feedingId);
    }
}
