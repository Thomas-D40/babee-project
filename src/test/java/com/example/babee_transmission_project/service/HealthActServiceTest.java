package com.example.babee_transmission_project.service;

import com.example.babee_transmission_project.converter.HealthActConverter;
import com.example.babee_transmission_project.entity.HealthActEntity;
import com.example.babee_transmission_project.exception.NotFoundException;
import com.example.babee_transmission_project.model.HealthActResource;
import com.example.babee_transmission_project.model.input.HealthActInputResource;
import com.example.babee_transmission_project.repository.BabeeRepository;
import com.example.babee_transmission_project.repository.HealthActRepository;
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
class HealthActServiceTest {

    private final String MISSING_ERROR = "Pas d'acte de santé avec l'id ";
    private final String MISSING_BABEE_ERROR = "BabeeId incorrect";
    @Mock
    private HealthActRepository healthActRepository;
    @Mock
    private BabeeRepository babeeRepository;
    private HealthActService healthActService;
    private UUID babeeId;
    private UUID healthActId;
    private LocalDate eventDate;
    private HealthActEntity healthActEntity;
    private HealthActResource healthActResource;
    private HealthActInputResource healthActInputResource;

    @BeforeEach
    void setUp() {
        healthActService = new HealthActService(healthActRepository, babeeRepository);

        babeeId = UUID.randomUUID();
        healthActId = UUID.randomUUID();
        eventDate = LocalDate.now();

        healthActEntity = HealthActEntity.builder()
                .id(healthActId)
                .babeeId(babeeId)
                .eventDate(eventDate)
                .healthActType(2)
                .medecine("Doliprane")
                .dosage("150 mg")
                .build();

        healthActResource = HealthActResource.builder()
                .id(healthActId)
                .babeeId(babeeId)
                .eventDate(eventDate)
                .healthActType(2)
                .medecine("Doliprane")
                .dosage("150 mg")
                .build();

        healthActInputResource = HealthActInputResource.builder()
                .babeeId(babeeId)
                .eventDate(eventDate)
                .healthActType(2)
                .medecine("Doliprane")
                .dosage("150 mg")
                .build();
    }

    @Test
    void getActivities_ShouldReturnListOfActivities() {
        when(healthActRepository.findAll((Specification<HealthActEntity>) any())).thenReturn(List.of(healthActEntity));

        try (var mocked = Mockito.mockStatic(HealthActConverter.class)) {
            mocked.when(() -> HealthActConverter.convertEntityToResource(healthActEntity))
                    .thenReturn(healthActResource);

            List<HealthActResource> result = healthActService.getHealthActs(babeeId, eventDate);

            assertThat(result).isNotNull().hasSize(1);
            assertThat(result.getFirst()).isEqualTo(healthActResource);
        }
    }

    @Test
    void getActivityById_WhenActivityExists_ShouldReturnActivity() {
        when(healthActRepository.findById(healthActId)).thenReturn(Optional.of(healthActEntity));

        try (var mocked = Mockito.mockStatic(HealthActConverter.class)) {
            mocked.when(() -> HealthActConverter.convertEntityToResource(healthActEntity))
                    .thenReturn(healthActResource);

            HealthActResource result = healthActService.getHealthActById(healthActId);

            assertThat(result).isEqualTo(healthActResource);
            verify(healthActRepository).findById(healthActId);
        }
    }

    @Test
    void getActivityById_WhenActivityDoesNotExist_ShouldThrowNotFoundException() {
        when(healthActRepository.findById(healthActId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> healthActService.getHealthActById(healthActId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_ERROR + healthActId);
    }

    @Test
    void saveActivity_ShouldSaveAndReturnActivity() {
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.of(mock()));

        try (var mocked = Mockito.mockStatic(HealthActConverter.class)) {
            mocked.when(() -> HealthActConverter.convertInputResourceToEntity(healthActInputResource))
                    .thenReturn(healthActEntity);
            mocked.when(() -> HealthActConverter.convertEntityToResource(healthActEntity))
                    .thenReturn(healthActResource);

            when(healthActRepository.saveAndFlush(healthActEntity)).thenReturn(healthActEntity);

            HealthActResource result = healthActService.saveHealthAct(healthActInputResource);

            assertThat(result).isEqualTo(healthActResource);
            verify(healthActRepository).saveAndFlush(healthActEntity);
        }
    }

    @Test
    void saveActivity_WhenBabeeDoesNotExist_ShouldThrowNotFoundException() {
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> healthActService.saveHealthAct(healthActInputResource))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_BABEE_ERROR);
    }

    @Test
    void updateActivity_WhenActivityExists_ShouldUpdateAndReturnActivity() {
        when(healthActRepository.findById(healthActId)).thenReturn(Optional.of(healthActEntity));
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.of(mock()));

        try (var mocked = Mockito.mockStatic(HealthActConverter.class)) {
            mocked.when(() -> HealthActConverter.convertInputResourceToEntity(healthActInputResource))
                    .thenReturn(healthActEntity);
            mocked.when(() -> HealthActConverter.convertEntityToResource(healthActEntity))
                    .thenReturn(healthActResource);

            when(healthActRepository.saveAndFlush(healthActEntity)).thenReturn(healthActEntity);

            HealthActResource result = healthActService.updateHealthAct(healthActId, healthActInputResource);

            assertThat(result).isEqualTo(healthActResource);
            verify(healthActRepository).saveAndFlush(healthActEntity);
        }
    }

    @Test
    void updateActivity_WhenActivityDoesNotExist_ShouldThrowNotFoundException() {
        when(healthActRepository.findById(healthActId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> healthActService.updateHealthAct(healthActId, healthActInputResource))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_ERROR + healthActId);
    }

    @Test
    void updateActivity_WhenBabeeDoesNotExist_ShouldThrowNotFoundException() {
        when(healthActRepository.findById(healthActId)).thenReturn(Optional.of(healthActEntity));
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> healthActService.updateHealthAct(healthActId, healthActInputResource))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_BABEE_ERROR);
    }

    @Test
    void deleteActivityById_WhenActivityExists_ShouldDeleteActivity() {
        when(healthActRepository.findById(healthActId)).thenReturn(Optional.of(healthActEntity));

        healthActService.deleteHealthActById(healthActId);

        verify(healthActRepository).delete(healthActEntity);
    }

    @Test
    void deleteActivityById_WhenActivityDoesNotExist_ShouldThrowNotFoundException() {
        when(healthActRepository.findById(healthActId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> healthActService.deleteHealthActById(healthActId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_ERROR + healthActId);
    }
}
