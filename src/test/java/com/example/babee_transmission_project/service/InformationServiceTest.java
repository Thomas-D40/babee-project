package com.example.babee_transmission_project.service;

import com.example.babee_transmission_project.converter.InformationConverter;
import com.example.babee_transmission_project.entity.InformationEntity;
import com.example.babee_transmission_project.exception.NotFoundException;
import com.example.babee_transmission_project.model.InformationResource;
import com.example.babee_transmission_project.model.input.InformationInputResource;
import com.example.babee_transmission_project.repository.BabeeRepository;
import com.example.babee_transmission_project.repository.InformationRepository;
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
class InformationServiceTest {

    private final String MISSING_ERROR = "Pas d'information avec l'id ";
    private final String MISSING_BABEE_ERROR = "BabeeId incorrect";
    @Mock
    private InformationRepository informationRepository;
    @Mock
    private BabeeRepository babeeRepository;
    private InformationService informationService;
    private UUID babeeId;
    private UUID informationId;
    private LocalDate eventDate;
    private InformationEntity informationEntity;
    private InformationResource informationResource;
    private InformationInputResource informationInputResource;

    @BeforeEach
    void setUp() {
        informationService = new InformationService(informationRepository, babeeRepository);

        babeeId = UUID.randomUUID();
        informationId = UUID.randomUUID();
        eventDate = LocalDate.now();

        informationEntity = InformationEntity.builder()
                .id(informationId)
                .babeeId(babeeId)
                .eventDate(eventDate)
                .commentaire("Test")
                .build();

        informationResource = InformationResource.builder()
                .id(informationId)
                .babeeId(babeeId)
                .eventDate(eventDate)
                .commentaire("Test")
                .build();

        informationInputResource = InformationInputResource.builder()
                .babeeId(babeeId)
                .eventDate(eventDate)
                .commentaire("Test")
                .build();
    }

    @Test
    void getActivities_ShouldReturnListOfActivities() {
        when(informationRepository.findAll((Specification<InformationEntity>) any())).thenReturn(List.of(informationEntity));

        try (var mocked = Mockito.mockStatic(InformationConverter.class)) {
            mocked.when(() -> InformationConverter.convertEntityToResource(informationEntity))
                    .thenReturn(informationResource);

            List<InformationResource> result = informationService.getInformations(babeeId, eventDate);

            assertThat(result).isNotNull().hasSize(1);
            assertThat(result.getFirst()).isEqualTo(informationResource);
        }
    }

    @Test
    void getActivityById_WhenActivityExists_ShouldReturnActivity() {
        when(informationRepository.findById(informationId)).thenReturn(Optional.of(informationEntity));

        try (var mocked = Mockito.mockStatic(InformationConverter.class)) {
            mocked.when(() -> InformationConverter.convertEntityToResource(informationEntity))
                    .thenReturn(informationResource);

            InformationResource result = informationService.getInformationById(informationId);

            assertThat(result).isEqualTo(informationResource);
            verify(informationRepository).findById(informationId);
        }
    }

    @Test
    void getActivityById_WhenActivityDoesNotExist_ShouldThrowNotFoundException() {
        when(informationRepository.findById(informationId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> informationService.getInformationById(informationId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_ERROR + informationId);
    }

    @Test
    void saveActivity_ShouldSaveAndReturnActivity() {
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.of(mock()));

        try (var mocked = Mockito.mockStatic(InformationConverter.class)) {
            mocked.when(() -> InformationConverter.convertInputResourceToEntity(informationInputResource))
                    .thenReturn(informationEntity);
            mocked.when(() -> InformationConverter.convertEntityToResource(informationEntity))
                    .thenReturn(informationResource);

            when(informationRepository.saveAndFlush(informationEntity)).thenReturn(informationEntity);

            InformationResource result = informationService.saveInformation(informationInputResource);

            assertThat(result).isEqualTo(informationResource);
            verify(informationRepository).saveAndFlush(informationEntity);
        }
    }

    @Test
    void saveActivity_WhenBabeeDoesNotExist_ShouldThrowNotFoundException() {
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> informationService.saveInformation(informationInputResource))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_BABEE_ERROR);
    }

    @Test
    void updateActivity_WhenActivityExists_ShouldUpdateAndReturnActivity() {
        when(informationRepository.findById(informationId)).thenReturn(Optional.of(informationEntity));
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.of(mock()));

        try (var mocked = Mockito.mockStatic(InformationConverter.class)) {
            mocked.when(() -> InformationConverter.convertInputResourceToEntity(informationInputResource))
                    .thenReturn(informationEntity);
            mocked.when(() -> InformationConverter.convertEntityToResource(informationEntity))
                    .thenReturn(informationResource);

            when(informationRepository.saveAndFlush(informationEntity)).thenReturn(informationEntity);

            InformationResource result = informationService.updateInformation(informationId, informationInputResource);

            assertThat(result).isEqualTo(informationResource);
            verify(informationRepository).saveAndFlush(informationEntity);
        }
    }

    @Test
    void updateActivity_WhenActivityDoesNotExist_ShouldThrowNotFoundException() {
        when(informationRepository.findById(informationId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> informationService.updateInformation(informationId, informationInputResource))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_ERROR + informationId);
    }

    @Test
    void updateActivity_WhenBabeeDoesNotExist_ShouldThrowNotFoundException() {
        when(informationRepository.findById(informationId)).thenReturn(Optional.of(informationEntity));
        when(babeeRepository.findById(babeeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> informationService.updateInformation(informationId, informationInputResource))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_BABEE_ERROR);
    }

    @Test
    void deleteActivityById_WhenActivityExists_ShouldDeleteActivity() {
        when(informationRepository.findById(informationId)).thenReturn(Optional.of(informationEntity));

        informationService.deleteInformationById(informationId);

        verify(informationRepository).delete(informationEntity);
    }

    @Test
    void deleteActivityById_WhenActivityDoesNotExist_ShouldThrowNotFoundException() {
        when(informationRepository.findById(informationId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> informationService.deleteInformationById(informationId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(MISSING_ERROR + informationId);
    }
}
