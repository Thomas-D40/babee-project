package com.example.babee_transmission_project.controller;

import com.example.babee_transmission_project.model.FeedingResource;
import com.example.babee_transmission_project.model.input.FeedingInputResource;
import com.example.babee_transmission_project.service.FeedingService;
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

@WebMvcTest(FeedingController.class)
class FeedingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedingService feedingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetFeedings() throws Exception {
        FeedingResource feeding = FeedingResource.builder()
                .id(UUID.randomUUID())
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .feedingInformations(List.of("Feeding Info"))
                .build();

        when(feedingService.getFeedings(any(), any())).thenReturn(List.of(feeding));

        mockMvc.perform(get("/feeding")
                        .param("babeeId", UUID.randomUUID().toString())
                        .param("day", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].feedingInformations[0]").value("Feeding Info"));

        verify(feedingService).getFeedings(any(), any());
    }

    @Test
    void shouldGetFeedingById() throws Exception {
        UUID id = UUID.randomUUID();
        FeedingResource feeding = FeedingResource.builder()
                .id(id)
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .feedingInformations(List.of("Feeding Info"))
                .build();

        when(feedingService.getFeedingById(id)).thenReturn(feeding);

        mockMvc.perform(get("/feeding/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedingInformations[0]").value("Feeding Info"));

        verify(feedingService).getFeedingById(id);
    }

    @Test
    void shouldSaveFeeding() throws Exception {
        FeedingInputResource input = FeedingInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .feedingInformations(List.of("Feeding Info"))
                .build();

        FeedingResource saved = FeedingResource.builder()
                .id(UUID.randomUUID())
                .babeeId(input.getBabeeId())
                .eventDate(input.getEventDate())
                .feedingInformations(input.getFeedingInformations())
                .build();

        when(feedingService.saveFeeding(any())).thenReturn(saved);

        mockMvc.perform(post("/feeding")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedingInformations[0]").value("Feeding Info"));

        verify(feedingService).saveFeeding(any());
    }

    @Test
    void shouldFailSavingFeedingWhenBabeeIdIsNull() throws Exception {
        FeedingInputResource input = FeedingInputResource.builder()
                .eventDate(LocalDate.now())
                .feedingInformations(List.of("Feeding Info"))
                .build();

        mockMvc.perform(post("/feeding")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.babeeId").value("must not be null"));
    }

    @Test
    void shouldFailSavingFeedingWhenEventDateIsNull() throws Exception {
        FeedingInputResource input = FeedingInputResource.builder()
                .babeeId(UUID.randomUUID())
                .feedingInformations(List.of("Feeding Info"))
                .build();

        mockMvc.perform(post("/feeding")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.eventDate").value("must not be null"));
    }

    @Test
    void shouldUpdateFeeding() throws Exception {
        UUID id = UUID.randomUUID();
        FeedingInputResource input = FeedingInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .feedingInformations(List.of("Updated Feeding Info"))
                .build();

        FeedingResource updated = FeedingResource.builder()
                .id(id)
                .babeeId(input.getBabeeId())
                .eventDate(input.getEventDate())
                .feedingInformations(input.getFeedingInformations())
                .build();

        when(feedingService.updateFeeding(eq(id), any())).thenReturn(updated);

        mockMvc.perform(put("/feeding/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedingInformations[0]").value("Updated Feeding Info"));

        verify(feedingService).updateFeeding(eq(id), any());
    }

    @Test
    void shouldFailUpdatingFeedingWhenBabeeIdIsNull() throws Exception {
        UUID id = UUID.randomUUID();
        FeedingInputResource input = FeedingInputResource.builder()
                .eventDate(LocalDate.now())
                .feedingInformations(List.of("Updated Feeding Info"))
                .build();

        mockMvc.perform(put("/feeding/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.babeeId").value("must not be null"));
    }

    @Test
    void shouldFailUpdatingFeedingWhenEventDateIsNull() throws Exception {
        UUID id = UUID.randomUUID();
        FeedingInputResource input = FeedingInputResource.builder()
                .babeeId(UUID.randomUUID())
                .feedingInformations(List.of("Updated Feeding Info"))
                .build();

        mockMvc.perform(put("/feeding/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.eventDate").value("must not be null"));
    }

    @Test
    void shouldDeleteFeedingById() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/feeding/{id}", id))
                .andExpect(status().isOk());

        verify(feedingService).deleteFeedingById(id);
    }
}
