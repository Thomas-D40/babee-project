package com.example.babee_transmission_project.controller;

import com.example.babee_transmission_project.model.BabeeResource;
import com.example.babee_transmission_project.model.input.BabeeInputResource;
import com.example.babee_transmission_project.service.BabeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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


@WebMvcTest(BabeeController.class)
class BabeeControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BabeeService babeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetActivities() throws Exception {
        BabeeResource babee = BabeeResource.builder()
                .id(UUID.randomUUID())
                .firstName("Titi")
                .lastName("Toto")
                .birthDate(LocalDate.of(2025, 01, 01))
                .build();

        when(babeeService.getBabees()).thenReturn(List.of(babee));

        mockMvc.perform(get("/babee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Toto"))
                .andExpect(jsonPath("$[0].prenom").value("Titi"))
                .andExpect(jsonPath("$[0].dateNaissance").value("2025-01-01")
                );

        verify(babeeService).getBabees();
    }

    @Test
    void shouldGetActivityById() throws Exception {
        UUID id = UUID.randomUUID();
        BabeeResource babee = BabeeResource.builder()
                .id(id)
                .firstName("Titi")
                .lastName("Toto")
                .birthDate(LocalDate.of(2025, 01, 01))
                .build();

        when(babeeService.getBabeeById(id)).thenReturn(babee);

        mockMvc.perform(get("/babee/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Toto"))
                .andExpect(jsonPath("$.prenom").value("Titi"))
                .andExpect(jsonPath("$.dateNaissance").value("2025-01-01")
                );

        verify(babeeService).getBabeeById(id);
    }

    @Test
    void shouldSaveActivity() throws Exception {
        BabeeInputResource input = BabeeInputResource.builder()
                .firstName("Titi")
                .lastName("Toto")
                .birthDate(LocalDate.of(2025, 01, 01))
                .build();

        BabeeResource saved = BabeeResource.builder()
                .id(UUID.randomUUID())
                .firstName("Titi")
                .lastName("Toto")
                .birthDate(LocalDate.of(2025, 01, 01))
                .build();

        when(babeeService.saveBabee(any())).thenReturn(saved);

        mockMvc.perform(post("/babee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Toto"))
                .andExpect(jsonPath("$.prenom").value("Titi"))
                .andExpect(jsonPath("$.dateNaissance").value("2025-01-01")
                );

        verify(babeeService).saveBabee(any());
    }

    @Test
    void shouldFailSavingActivityWhenNomIsNull() throws Exception {
        BabeeInputResource input = BabeeInputResource.builder()
                .firstName("Titi")
                .birthDate(LocalDate.of(2025, 01, 01))
                .build();

        mockMvc.perform(post("/babee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nom").value("must not be null"));
    }

    @Test
    void shouldFailSavingActivityWhenPrenomIsNull() throws Exception {
        BabeeInputResource input = BabeeInputResource.builder()
                .lastName("Titi")
                .birthDate(LocalDate.of(2025, 01, 01))
                .build();

        mockMvc.perform(post("/babee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.prenom").value("must not be null"));
    }

    @Test
    void shouldFailSavingActivityWhenDateNaissanceIsNull() throws Exception {
        BabeeInputResource input = BabeeInputResource.builder()
                .firstName("Titi")
                .lastName("Toto")
                .build();

        mockMvc.perform(post("/babee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dateNaissance").value("must not be null"));
    }


    @Test
    void shouldUpdateActivity() throws Exception {
        UUID id = UUID.randomUUID();
        BabeeInputResource input = BabeeInputResource.builder()
                .firstName("Titi")
                .lastName("Toto")
                .birthDate(LocalDate.of(2025, 01, 01))
                .build();

        BabeeResource updated = BabeeResource.builder()
                .id(id)
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .birthDate(input.getBirthDate())
                .build();

        when(babeeService.updateBabee(eq(id), any())).thenReturn(updated);

        mockMvc.perform(put("/babee/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Toto"))
                .andExpect(jsonPath("$.prenom").value("Titi"))
                .andExpect(jsonPath("$.dateNaissance").value("2025-01-01"));

        verify(babeeService).updateBabee(eq(id), any());
    }

    @Test
    void shouldFailUpdatingActivityWhenNomIsNull() throws Exception {
        UUID id = UUID.randomUUID();
        BabeeInputResource input = BabeeInputResource.builder()
                .firstName("Titi")
                .birthDate(LocalDate.of(2025, 01, 01))
                .build();

        mockMvc.perform(put("/babee/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nom").value("must not be null"));
    }

    @Test
    void shouldFailUpdatingActivityWhenPrenomIsNull() throws Exception {
        UUID id = UUID.randomUUID();
        BabeeInputResource input = BabeeInputResource.builder()
                .lastName("Titi")
                .birthDate(LocalDate.of(2025, 01, 01))
                .build();

        mockMvc.perform(put("/babee/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.prenom").value("must not be null"));
    }

    @Test
    void shouldFailUpdatingActivityWhenDateNaissanceIsNull() throws Exception {
        UUID id = UUID.randomUUID();
        BabeeInputResource input = BabeeInputResource.builder()
                .firstName("Titi")
                .lastName("Toto")
                .build();

        mockMvc.perform(put("/babee/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dateNaissance").value("must not be null"));
    }

    @Test
    void shouldDeleteActivityById() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/babee/{id}", id))
                .andExpect(status().isOk());

        verify(babeeService).deleteBabeeById(id);
    }
}
