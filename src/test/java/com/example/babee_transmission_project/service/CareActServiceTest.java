package com.example.babee_transmission_project.service;

import com.example.babee_transmission_project.converter.CareActConverter;
import com.example.babee_transmission_project.entity.CareActEntity;
import com.example.babee_transmission_project.exception.NotFoundException;
import com.example.babee_transmission_project.model.CareActResource;
import com.example.babee_transmission_project.model.input.CareActInputResource;
import com.example.babee_transmission_project.repository.BabeeRepository;
import com.example.babee_transmission_project.repository.CareActRepository;
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
class CareActServiceTest {

    private final String MISSING_ERROR = "Pas d'acte de soin avec l'id ";
    private final String MISSING_BABEE_ERROR = "BabeeId incorrect";
    @Mock
    private CareActRepository careActRepository;
    @Mock
    private BabeeRepository babeeRepository;
    private CareActService careActService;
    private UUID babeeId;
    private UUID careActId;
    private LocalDate eventDate;
    private CareActEntity careActEntity;
    private CareActResource careActResource;
    private CareActInputResource careActInputResource;

    @BeforeEach
    void setUp() {
        careActService = new CareActService(careActRepository, babeeRepository);

        babeeId = UUID.randomUUID();
        careActId = UUID.randomUUID();
        eventDate = LocalDate.now();

        careActEntity = CareActEntity.builder()
                .id(careActId)
                .babeeId(babeeId)
                .eventDate(eventDate)
                .careActType(1)
                .careActDetail(2)
                .build();

        careActResource = CareActResource.builder()
                .id(careActId)
                .babeeId(babeeId)
                .eventDate(eventDate)
                .careActType(1)
                .careActDetail(2)
                .build();

        careActInputResource = CareActInputResource.builder()
                .babeeId(babeeId)
                .eventDate(eventDate)
                .careActType(1)
                .careActDetail(2)
                .build();
    }

    @Test
    void getActivities_ShouldReturnListOfActivities() {
        when(careActRepository.findAll((Specification<CareActEntity>) any())).thenReturn(List.of(careActEntity));

        try (var mocked = Mockito.mockStatic(CareActConverter.class)) {
            mocked.when(() -> CareActConverter.convertEntityToResource(careActEntity))
                    .thenReturn(careActResource);

            List<CareActResource> result = careActService.getCareActs(babeeId, eventDate);

            assertThat(result).isNotNull().hasSize(1);
            assertThat(result.getFirst()).isEqualTo(careActResource);
        }
    }

    @Test
    void getActivityById_WhenActivityExists_ShouldReturnActivity() {
        when(careActRepository.findById(careActId)).thenReturn(Optional.of(careActEntity));

        try (var mocked = Mockito.mockStatic(CareActConverter.class)) {
            mocked.when(() -> CareActConverter.convertEntityToResource(careActEntity))
                    .thenReturn(careActResource);

            CareActResource result = careActService.getCareActById(careActId);

            assertThat(result).isEqualTo(careActResource);
            verify(careActRepository).findById(careActId);
        }
    }

    @Test
    void getActivityById_WhenActivityDoesNotExist_ShouldThrowNotFoundException() {
        when(careActRepository.findById(careActId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> careActService.getCareActById(careActId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_ERROR + careActId);
    }

    @Test
    void saveActivity_ShouldSaveAndReturnActivity() {
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.of(mock()));

        try (var mocked = Mockito.mockStatic(CareActConverter.class)) {
            mocked.when(() -> CareActConverter.convertInputResourceToEntity(careActInputResource))
                    .thenReturn(careActEntity);
            mocked.when(() -> CareActConverter.convertEntityToResource(careActEntity))
                    .thenReturn(careActResource);

            when(careActRepository.saveAndFlush(careActEntity)).thenReturn(careActEntity);

            CareActResource result = careActService.saveCareAct(careActInputResource);

            assertThat(result).isEqualTo(careActResource);
            verify(careActRepository).saveAndFlush(careActEntity);
        }
    }

    @Test
    void saveActivity_WhenBabeeDoesNotExist_ShouldThrowNotFoundException() {
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> careActService.saveCareAct(careActInputResource))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_BABEE_ERROR);
    }

    @Test
    void updateActivity_WhenActivityExists_ShouldUpdateAndReturnActivity() {
        when(careActRepository.findById(careActId)).thenReturn(Optional.of(careActEntity));
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.of(mock()));

        try (var mocked = Mockito.mockStatic(CareActConverter.class)) {
            mocked.when(() -> CareActConverter.convertInputResourceToEntity(careActInputResource))
                    .thenReturn(careActEntity);
            mocked.when(() -> CareActConverter.convertEntityToResource(careActEntity))
                    .thenReturn(careActResource);

            when(careActRepository.saveAndFlush(careActEntity)).thenReturn(careActEntity);

            CareActResource result = careActService.updateCareAct(careActId, careActInputResource);

            assertThat(result).isEqualTo(careActResource);
            verify(careActRepository).saveAndFlush(careActEntity);
        }
    }

    @Test
    void updateActivity_WhenActivityDoesNotExist_ShouldThrowNotFoundException() {
        when(careActRepository.findById(careActId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> careActService.updateCareAct(careActId, careActInputResource))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_ERROR + careActId);
    }

    @Test
    void updateActivity_WhenBabeeDoesNotExist_ShouldThrowNotFoundException() {
        when(careActRepository.findById(careActId)).thenReturn(Optional.of(careActEntity));
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> careActService.updateCareAct(careActId, careActInputResource))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_BABEE_ERROR);
    }

    @Test
    void deleteActivityById_WhenActivityExists_ShouldDeleteActivity() {
        when(careActRepository.findById(careActId)).thenReturn(Optional.of(careActEntity));

        careActService.deleteCareActById(careActId);

        verify(careActRepository).delete(careActEntity);
    }

    @Test
    void deleteActivityById_WhenActivityDoesNotExist_ShouldThrowNotFoundException() {
        when(careActRepository.findById(careActId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> careActService.deleteCareActById(careActId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_ERROR + careActId);
    }
}
