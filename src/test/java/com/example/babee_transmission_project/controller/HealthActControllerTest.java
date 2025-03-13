package com.example.babee_transmission_project.controller;

import com.example.babee_transmission_project.model.HealthActResource;
import com.example.babee_transmission_project.model.input.HealthActInputResource;
import com.example.babee_transmission_project.service.HealthActService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HealthActController.class)
class HealthActControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HealthActService healthActService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetHealthActs() throws Exception {
        HealthActResource healthAct = HealthActResource.builder()
                .id(UUID.randomUUID())
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .healthActType(1)
                .temperature(37)
                .build();

        when(healthActService.getHealthActs(any(), any())).thenReturn(List.of(healthAct));

        mockMvc.perform(get("/health-act")
                        .param("babeeId", UUID.randomUUID().toString())
                        .param("day", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].healthActType").value(1))
                .andExpect(jsonPath("$[0].temperature").value(37));

        verify(healthActService).getHealthActs(any(), any());
    }

    @Test
    void shouldGetHealthActById() throws Exception {
        UUID id = UUID.randomUUID();
        HealthActResource healthAct = HealthActResource.builder()
                .id(id)
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .healthActType(1)
                .temperature(37)
                .build();

        when(healthActService.getHealthActById(id)).thenReturn(healthAct);

        mockMvc.perform(get("/health-act/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.healthActType").value(1))
                .andExpect(jsonPath("$.temperature").value(37));

        verify(healthActService).getHealthActById(id);
    }

    @Test
    void shouldSaveHealthAct() throws Exception {
        HealthActInputResource input = HealthActInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .healthActType(1)
                .temperature(37)
                .actHour(Time.valueOf("22:00:00"))
                .build();

        HealthActResource saved = HealthActResource.builder()
                .id(UUID.randomUUID())
                .babeeId(input.getBabeeId())
                .eventDate(input.getEventDate())
                .healthActType(input.getHealthActType())
                .temperature(input.getTemperature())
                .actHour(Time.valueOf("22:00:00"))
                .build();

        when(healthActService.saveHealthAct(any())).thenReturn(saved);

        mockMvc.perform(post("/health-act")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.healthActType").value(1));

        verify(healthActService).saveHealthAct(any());
    }

    @Test
    void shouldFailSavingHealthActWhenBabeeIdIsNull() throws Exception {
        HealthActInputResource input = HealthActInputResource.builder()
                .eventDate(LocalDate.now())
                .healthActType(1)
                .temperature(37)
                .build();

        mockMvc.perform(post("/health-act")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.babeeId").value("must not be null"));
    }

    @Test
    void shouldFailSavingHealthActWhenEventDateIsNull() throws Exception {
        HealthActInputResource input = HealthActInputResource.builder()
                .babeeId(UUID.randomUUID())
                .healthActType(1)
                .temperature(37)
                .build();

        mockMvc.perform(post("/health-act")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.eventDate").value("must not be null"));
    }

    @Test
    void shouldFailSavingHealthActWhenHealthActTypeIsInvalid() throws Exception {
        HealthActInputResource input = HealthActInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .healthActType(3)  // Invalid type
                .temperature(37)
                .build();

        mockMvc.perform(post("/health-act")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.healthActType").value("Type d'acte de soin invalide"));
    }

    @Test
    void shouldFailSavingHealthActWhenTemperatureAndMedicamentsArePresent() throws Exception {
        HealthActInputResource input = HealthActInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .healthActType(1)
                .temperature(37)
                .medecine("Paracetamol")
                .dosage("500mg")
                .build();

        mockMvc.perform(post("/health-act")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.healthActType").value("Température & Médicaments ne peuvent être indiqués ensemble."));
    }

    @Test
    void shouldFailSavingHealthActWhenMissingTemperatureForHealthActType1() throws Exception {
        HealthActInputResource input = HealthActInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .healthActType(1)  // Type 1 requires temperature
                .build();

        mockMvc.perform(post("/health-act")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.temperature").value("La température doit être indiquée."));
    }

    @Test
    void shouldFailSavingHealthActWhenMissingMedicationForHealthActType2() throws Exception {
        HealthActInputResource input = HealthActInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .healthActType(2)  // Type 2 requires medication info
                .build();

        mockMvc.perform(post("/health-act")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.healthActType").value("Il manque le nom du médicament et/ou le dosage"));
    }

    @Test
    void shouldUpdateHealthAct() throws Exception {
        UUID id = UUID.randomUUID();
        HealthActInputResource input = HealthActInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .healthActType(1)
                .temperature(38)
                .actHour(Time.valueOf("22:00:00"))
                .build();

        HealthActResource updated = HealthActResource.builder()
                .id(id)
                .babeeId(input.getBabeeId())
                .eventDate(input.getEventDate())
                .healthActType(input.getHealthActType())
                .temperature(input.getTemperature())
                .actHour(Time.valueOf("22:00:00"))
                .build();

        when(healthActService.updateHealthAct(eq(id), any())).thenReturn(updated);

        mockMvc.perform(put("/health-act/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.healthActType").value(1));

        verify(healthActService).updateHealthAct(eq(id), any());
    }

    @Test
    void shouldDeleteHealthActById() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/health-act/{id}", id))
                .andExpect(status().isOk());

        verify(healthActService).deleteHealthActById(id);
    }
}
