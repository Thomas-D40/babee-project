package com.example.babee_transmission_project.service;

import com.example.babee_transmission_project.converter.SleepingConverter;
import com.example.babee_transmission_project.entity.SleepingEntity;
import com.example.babee_transmission_project.exception.NotFoundException;
import com.example.babee_transmission_project.model.SleepingResource;
import com.example.babee_transmission_project.model.input.SleepingInputResource;
import com.example.babee_transmission_project.repository.BabeeRepository;
import com.example.babee_transmission_project.repository.SleepingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SleepingServiceTest {

    private final String MISSING_ERROR = "Pas de sommeil avec l'id ";
    private final String MISSING_BABEE_ERROR = "BabeeId incorrect";
    @Mock
    private SleepingRepository sleepingRepository;
    @Mock
    private BabeeRepository babeeRepository;
    private SleepingService sleepingService;
    private UUID babeeId;
    private UUID sleepingId;
    private LocalDate eventDate;
    private SleepingEntity sleepingEntity;
    private SleepingResource sleepingResource;
    private SleepingInputResource sleepingInputResource;

    @BeforeEach
    void setUp() {
        sleepingService = new SleepingService(sleepingRepository, babeeRepository);

        babeeId = UUID.randomUUID();
        sleepingId = UUID.randomUUID();
        eventDate = LocalDate.now();

        sleepingEntity = SleepingEntity.builder()
                .id(sleepingId)
                .babeeId(babeeId)
                .eventDate(eventDate)
                .debut(Time.valueOf("06:30:00"))
                .fin(Time.valueOf("07:15:00"))
                .build();

        sleepingResource = SleepingResource.builder()
                .id(sleepingId)
                .babeeId(babeeId)
                .eventDate(eventDate)
                .debut(Time.valueOf("06:30:00"))
                .fin(Time.valueOf("07:15:00"))
                .build();

        sleepingInputResource = SleepingInputResource.builder()
                .babeeId(babeeId)
                .eventDate(eventDate)
                .debut(Time.valueOf("06:30:00"))
                .fin(Time.valueOf("07:15:00"))
                .build();
    }

    @Test
    void getActivities_ShouldReturnListOfActivities() {
        when(sleepingRepository.findAll((Specification<SleepingEntity>) any())).thenReturn(List.of(sleepingEntity));

        try (var mocked = Mockito.mockStatic(SleepingConverter.class)) {
            mocked.when(() -> SleepingConverter.convertEntityToResource(sleepingEntity))
                    .thenReturn(sleepingResource);

            List<SleepingResource> result = sleepingService.getSleepings(babeeId, eventDate);

            assertThat(result).isNotNull().hasSize(1);
            assertThat(result.getFirst()).isEqualTo(sleepingResource);
        }
    }

    @Test
    void getActivityById_WhenActivityExists_ShouldReturnActivity() {
        when(sleepingRepository.findById(sleepingId)).thenReturn(Optional.of(sleepingEntity));

        try (var mocked = Mockito.mockStatic(SleepingConverter.class)) {
            mocked.when(() -> SleepingConverter.convertEntityToResource(sleepingEntity))
                    .thenReturn(sleepingResource);

            SleepingResource result = sleepingService.getSleepingById(sleepingId);

            assertThat(result).isEqualTo(sleepingResource);
            verify(sleepingRepository).findById(sleepingId);
        }
    }

    @Test
    void getActivityById_WhenActivityDoesNotExist_ShouldThrowNotFoundException() {
        when(sleepingRepository.findById(sleepingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sleepingService.getSleepingById(sleepingId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_ERROR + sleepingId);
    }

    @Test
    void saveActivity_ShouldSaveAndReturnActivity() {
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.of(mock()));

        try (var mocked = Mockito.mockStatic(SleepingConverter.class)) {
            mocked.when(() -> SleepingConverter.convertInputResourceToEntity(sleepingInputResource))
                    .thenReturn(sleepingEntity);
            mocked.when(() -> SleepingConverter.convertEntityToResource(sleepingEntity))
                    .thenReturn(sleepingResource);

            when(sleepingRepository.saveAndFlush(sleepingEntity)).thenReturn(sleepingEntity);

            SleepingResource result = sleepingService.saveSleeping(sleepingInputResource);

            assertThat(result).isEqualTo(sleepingResource);
            verify(sleepingRepository).saveAndFlush(sleepingEntity);
        }
    }

    @Test
    void saveActivity_WhenBabeeDoesNotExist_ShouldThrowNotFoundException() {
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sleepingService.saveSleeping(sleepingInputResource))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_BABEE_ERROR);
    }

    @Test
    void updateActivity_WhenActivityExists_ShouldUpdateAndReturnActivity() {
        when(sleepingRepository.findById(sleepingId)).thenReturn(Optional.of(sleepingEntity));
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.of(mock()));

        try (var mocked = Mockito.mockStatic(SleepingConverter.class)) {
            mocked.when(() -> SleepingConverter.convertInputResourceToEntity(sleepingInputResource))
                    .thenReturn(sleepingEntity);
            mocked.when(() -> SleepingConverter.convertEntityToResource(sleepingEntity))
                    .thenReturn(sleepingResource);

            when(sleepingRepository.saveAndFlush(sleepingEntity)).thenReturn(sleepingEntity);

            SleepingResource result = sleepingService.updateSleeping(sleepingId, sleepingInputResource);

            assertThat(result).isEqualTo(sleepingResource);
            verify(sleepingRepository).saveAndFlush(sleepingEntity);
        }
    }

    @Test
    void updateActivity_WhenActivityDoesNotExist_ShouldThrowNotFoundException() {
        when(sleepingRepository.findById(sleepingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sleepingService.updateSleeping(sleepingId, sleepingInputResource))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_ERROR + sleepingId);
    }

    @Test
    void updateActivity_WhenBabeeDoesNotExist_ShouldThrowNotFoundException() {
        when(sleepingRepository.findById(sleepingId)).thenReturn(Optional.of(sleepingEntity));
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sleepingService.updateSleeping(sleepingId, sleepingInputResource))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_BABEE_ERROR);
    }

    @Test
    void deleteActivityById_WhenActivityExists_ShouldDeleteActivity() {
        when(sleepingRepository.findById(sleepingId)).thenReturn(Optional.of(sleepingEntity));

        sleepingService.deleteSleepingById(sleepingId);

        verify(sleepingRepository).delete(sleepingEntity);
    }

    @Test
    void deleteActivityById_WhenActivityDoesNotExist_ShouldThrowNotFoundException() {
        when(sleepingRepository.findById(sleepingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sleepingService.deleteSleepingById(sleepingId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_ERROR + sleepingId);
    }
}
