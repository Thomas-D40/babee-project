package com.example.babee_transmission_project.service;

import com.example.babee_transmission_project.converter.ActivityConverter;
import com.example.babee_transmission_project.entity.ActivityEntity;
import com.example.babee_transmission_project.exception.NotFoundException;
import com.example.babee_transmission_project.model.ActivityResource;
import com.example.babee_transmission_project.model.input.ActivityInputResource;
import com.example.babee_transmission_project.repository.ActivityRepository;
import com.example.babee_transmission_project.repository.BabeeRepository;
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
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private BabeeRepository babeeRepository;

    private ActivityService activityService;

    private UUID babeeId;
    private UUID activityId;
    private LocalDate eventDate;
    private ActivityEntity activityEntity;
    private ActivityResource activityResource;
    private ActivityInputResource activityInputResource;

    @BeforeEach
    void setUp() {
        activityService = new ActivityService(activityRepository, babeeRepository);

        babeeId = UUID.randomUUID();
        activityId = UUID.randomUUID();
        eventDate = LocalDate.now();

        activityEntity = ActivityEntity.builder()
                .id(activityId)
                .babeeId(babeeId)
                .eventDate(eventDate)
                .name("Morpion")
                .build();

        activityResource = ActivityResource.builder()
                .id(activityId)
                .babeeId(babeeId)
                .eventDate(eventDate)
                .name("Ballon")
                .commentaire("avec Maël")
                .build();

        activityInputResource = ActivityInputResource.builder()
                .babeeId(babeeId)
                .eventDate(eventDate)
                .name("Repas")
                .build();
    }

    @Test
    void getActivities_ShouldReturnListOfActivities() {
        when(activityRepository.findAll((Specification<ActivityEntity>) any())).thenReturn(List.of(activityEntity));

        try (var mocked = Mockito.mockStatic(ActivityConverter.class)) {
            mocked.when(() -> ActivityConverter.convertEntityToResource(activityEntity))
                    .thenReturn(activityResource);

            List<ActivityResource> result = activityService.getActivities(babeeId, eventDate);

            assertThat(result).isNotNull().hasSize(1);
            assertThat(result.getFirst()).isEqualTo(activityResource);
        }
    }

    @Test
    void getActivityById_WhenActivityExists_ShouldReturnActivity() {
        when(activityRepository.findById(activityId)).thenReturn(Optional.of(activityEntity));

        try (var mocked = Mockito.mockStatic(ActivityConverter.class)) {
            mocked.when(() -> ActivityConverter.convertEntityToResource(activityEntity))
                    .thenReturn(activityResource);

            ActivityResource result = activityService.getActivityById(activityId);

            assertThat(result).isEqualTo(activityResource);
            verify(activityRepository).findById(activityId);
        }
    }

    @Test
    void getActivityById_WhenActivityDoesNotExist_ShouldThrowNotFoundException() {
        when(activityRepository.findById(activityId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> activityService.getActivityById(activityId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Pas d'activité avec l'id " + activityId);
    }

    @Test
    void saveActivity_ShouldSaveAndReturnActivity() {
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.of(mock()));

        try (var mocked = Mockito.mockStatic(ActivityConverter.class)) {
            mocked.when(() -> ActivityConverter.convertInputResourceToEntity(activityInputResource))
                    .thenReturn(activityEntity);
            mocked.when(() -> ActivityConverter.convertEntityToResource(activityEntity))
                    .thenReturn(activityResource);

            when(activityRepository.saveAndFlush(activityEntity)).thenReturn(activityEntity);

            ActivityResource result = activityService.saveActivity(activityInputResource);

            assertThat(result).isEqualTo(activityResource);
            verify(activityRepository).saveAndFlush(activityEntity);
        }
    }

    @Test
    void saveActivity_WhenBabeeDoesNotExist_ShouldThrowNotFoundException() {
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> activityService.saveActivity(activityInputResource))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("BabeeId incorrect");
    }

    @Test
    void updateActivity_WhenActivityExists_ShouldUpdateAndReturnActivity() {
        when(activityRepository.findById(activityId)).thenReturn(Optional.of(activityEntity));
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.of(mock()));

        try (var mocked = Mockito.mockStatic(ActivityConverter.class)) {
            mocked.when(() -> ActivityConverter.convertInputResourceToEntity(activityInputResource))
                    .thenReturn(activityEntity);
            mocked.when(() -> ActivityConverter.convertEntityToResource(activityEntity))
                    .thenReturn(activityResource);

            when(activityRepository.saveAndFlush(activityEntity)).thenReturn(activityEntity);

            ActivityResource result = activityService.updateActivity(activityId, activityInputResource);

            assertThat(result).isEqualTo(activityResource);
            verify(activityRepository).saveAndFlush(activityEntity);
        }
    }

    @Test
    void updateActivity_WhenActivityDoesNotExist_ShouldThrowNotFoundException() {
        when(activityRepository.findById(activityId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> activityService.updateActivity(activityId, activityInputResource))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Pas d'activité avec l'id " + activityId);
    }

    @Test
    void updateActivity_WhenBabeeDoesNotExist_ShouldThrowNotFoundException() {
        when(activityRepository.findById(activityId)).thenReturn(Optional.of(activityEntity));
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> activityService.updateActivity(activityId, activityInputResource))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("BabeeId incorrect");
    }

    @Test
    void deleteActivityById_WhenActivityExists_ShouldDeleteActivity() {
        when(activityRepository.findById(activityId)).thenReturn(Optional.of(activityEntity));

        activityService.deleteActivityById(activityId);

        verify(activityRepository).delete(activityEntity);
    }

    @Test
    void deleteActivityById_WhenActivityDoesNotExist_ShouldThrowNotFoundException() {
        when(activityRepository.findById(activityId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> activityService.deleteActivityById(activityId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Pas d'activité avec l'id " + activityId);
    }
}
