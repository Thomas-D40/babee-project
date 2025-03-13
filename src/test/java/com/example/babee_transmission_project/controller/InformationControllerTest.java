package com.example.babee_transmission_project.controller;

import com.example.babee_transmission_project.model.InformationResource;
import com.example.babee_transmission_project.model.input.InformationInputResource;
import com.example.babee_transmission_project.service.InformationService;
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

@WebMvcTest(InformationController.class)
class InformationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InformationService informationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetInformations() throws Exception {
        InformationResource information = InformationResource.builder()
                .id(UUID.randomUUID())
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .commentaire("Information commentaire")
                .build();

        when(informationService.getInformations(any(), any())).thenReturn(List.of(information));

        mockMvc.perform(get("/information")
                        .param("babeeId", UUID.randomUUID().toString())
                        .param("day", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].commentaire").value("Information commentaire"));

        verify(informationService).getInformations(any(), any());
    }

    @Test
    void shouldGetInformationById() throws Exception {
        UUID id = UUID.randomUUID();
        InformationResource information = InformationResource.builder()
                .id(id)
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .commentaire("Information commentaire")
                .build();

        when(informationService.getInformationById(id)).thenReturn(information);

        mockMvc.perform(get("/information/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentaire").value("Information commentaire"));

        verify(informationService).getInformationById(id);
    }

    @Test
    void shouldSaveInformation() throws Exception {
        InformationInputResource input = InformationInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .comment("Information commentaire")
                .build();

        InformationResource saved = InformationResource.builder()
                .id(UUID.randomUUID())
                .babeeId(input.getBabeeId())
                .eventDate(input.getEventDate())
                .commentaire(input.getComment())
                .build();

        when(informationService.saveInformation(any())).thenReturn(saved);

        mockMvc.perform(post("/information")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentaire").value("Information commentaire"));

        verify(informationService).saveInformation(any());
    }

    @Test
    void shouldFailSavingInformationWhenBabeeIdIsNull() throws Exception {
        InformationInputResource input = InformationInputResource.builder()
                .eventDate(LocalDate.now())
                .comment("Information commentaire")
                .build();

        mockMvc.perform(post("/information")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.babeeId").value("must not be null"));
    }

    @Test
    void shouldFailSavingInformationWhenEventDateIsNull() throws Exception {
        InformationInputResource input = InformationInputResource.builder()
                .babeeId(UUID.randomUUID())
                .comment("Information commentaire")
                .build();

        mockMvc.perform(post("/information")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.eventDate").value("must not be null"));
    }

    @Test
    void shouldUpdateInformation() throws Exception {
        UUID id = UUID.randomUUID();
        InformationInputResource input = InformationInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .comment("Updated Information commentaire")
                .build();

        InformationResource updated = InformationResource.builder()
                .id(id)
                .babeeId(input.getBabeeId())
                .eventDate(input.getEventDate())
                .commentaire(input.getComment())
                .build();

        when(informationService.updateInformation(eq(id), any())).thenReturn(updated);

        mockMvc.perform(put("/information/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentaire").value("Updated Information commentaire"));

        verify(informationService).updateInformation(eq(id), any());
    }

    @Test
    void shouldFailUpdatingInformationWhenBabeeIdIsNull() throws Exception {
        UUID id = UUID.randomUUID();
        InformationInputResource input = InformationInputResource.builder()
                .eventDate(LocalDate.now())
                .comment("Updated Information commentaire")
                .build();

        mockMvc.perform(put("/information/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.babeeId").value("must not be null"));
    }

    @Test
    void shouldFailUpdatingInformationWhenEventDateIsNull() throws Exception {
        UUID id = UUID.randomUUID();
        InformationInputResource input = InformationInputResource.builder()
                .babeeId(UUID.randomUUID())
                .comment("Updated Information commentaire")
                .build();

        mockMvc.perform(put("/information/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.eventDate").value("must not be null"));
    }

    @Test
    void shouldDeleteInformationById() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/information/{id}", id))
                .andExpect(status().isOk());

        verify(informationService).deleteInformationById(id);
    }
}
