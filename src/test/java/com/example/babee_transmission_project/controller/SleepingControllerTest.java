package com.example.babee_transmission_project.controller;

import com.example.babee_transmission_project.model.SleepingResource;
import com.example.babee_transmission_project.model.input.SleepingInputResource;
import com.example.babee_transmission_project.service.SleepingService;
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

@WebMvcTest(SleepingController.class)
class SleepingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SleepingService sleepingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetSleepings() throws Exception {
        SleepingResource sleeping = SleepingResource.builder()
                .id(UUID.randomUUID())
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .debut(Time.valueOf("22:00:00"))
                .fin(Time.valueOf("23:00:00"))
                .build();

        when(sleepingService.getSleepings(any(), any())).thenReturn(List.of(sleeping));

        mockMvc.perform(get("/sleeping")
                        .param("babeeId", UUID.randomUUID().toString())
                        .param("day", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].debut").value("22:00:00"))
                .andExpect(jsonPath("$[0].fin").value("23:00:00"));

        verify(sleepingService).getSleepings(any(), any());
    }

    @Test
    void shouldGetSleepingById() throws Exception {
        UUID id = UUID.randomUUID();
        SleepingResource sleeping = SleepingResource.builder()
                .id(id)
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .debut(Time.valueOf("22:00:00"))
                .fin(Time.valueOf("23:00:00"))
                .build();

        when(sleepingService.getSleepingById(id)).thenReturn(sleeping);

        mockMvc.perform(get("/sleeping/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.debut").value("22:00:00"))
                .andExpect(jsonPath("$.fin").value("23:00:00"));

        verify(sleepingService).getSleepingById(id);
    }

    @Test
    void shouldSaveSleeping() throws Exception {
        SleepingInputResource input = SleepingInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .debut(Time.valueOf("22:00:00"))
                .fin(Time.valueOf("23:00:00"))
                .build();

        SleepingResource saved = SleepingResource.builder()
                .id(UUID.randomUUID())
                .babeeId(input.getBabeeId())
                .eventDate(input.getEventDate())
                .debut(input.getDebut())
                .fin(input.getFin())
                .build();

        when(sleepingService.saveSleeping(any())).thenReturn(saved);

        mockMvc.perform(post("/sleeping")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.debut").value("22:00:00"))
                .andExpect(jsonPath("$.fin").value("23:00:00"));

        verify(sleepingService).saveSleeping(any());
    }

    @Test
    void shouldFailSavingSleepingWhenBabeeIdIsNull() throws Exception {
        SleepingInputResource input = SleepingInputResource.builder()
                .eventDate(LocalDate.now())
                .debut(Time.valueOf("22:00:00"))
                .fin(Time.valueOf("23:00:00"))
                .build();

        mockMvc.perform(post("/sleeping")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.babeeId").value("must not be null"));
    }

    @Test
    void shouldFailSavingSleepingWhenEventDateIsNull() throws Exception {
        SleepingInputResource input = SleepingInputResource.builder()
                .babeeId(UUID.randomUUID())
                .debut(Time.valueOf("22:00:00"))
                .fin(Time.valueOf("23:00:00"))
                .build();

        mockMvc.perform(post("/sleeping")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.eventDate").value("must not be null"));
    }

    @Test
    void shouldFailSavingSleepingWhenDebutIsNull() throws Exception {
        SleepingInputResource input = SleepingInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .fin(Time.valueOf("23:00:00"))
                .build();

        mockMvc.perform(post("/sleeping")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.debut").value("Ne peut être null."));
    }

    @Test
    void shouldFailSavingSleepingWhenFinIsNull() throws Exception {
        SleepingInputResource input = SleepingInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .debut(Time.valueOf("22:00:00"))
                .build();

        mockMvc.perform(post("/sleeping")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fin").value("Ne peut être null."));
    }

    @Test
    void shouldFailSavingSleepingWhenDebutAfterFin() throws Exception {
        SleepingInputResource input = SleepingInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .debut(Time.valueOf("23:00:00"))
                .fin(Time.valueOf("22:00:00"))
                .build();

        mockMvc.perform(post("/sleeping")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.debut").value("Les temps ne sont pas cohérents"));
    }

    @Test
    void shouldUpdateSleeping() throws Exception {
        UUID id = UUID.randomUUID();
        SleepingInputResource input = SleepingInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .debut(Time.valueOf("22:00:00"))
                .fin(Time.valueOf("23:00:00"))
                .build();

        SleepingResource updated = SleepingResource.builder()
                .id(id)
                .babeeId(input.getBabeeId())
                .eventDate(input.getEventDate())
                .debut(input.getDebut())
                .fin(input.getFin())
                .build();

        when(sleepingService.updateSleeping(eq(id), any())).thenReturn(updated);

        mockMvc.perform(put("/sleeping/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.debut").value("22:00:00"))
                .andExpect(jsonPath("$.fin").value("23:00:00"));

        verify(sleepingService).updateSleeping(eq(id), any());
    }

    @Test
    void shouldDeleteSleepingById() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/sleeping/{id}", id))
                .andExpect(status().isOk());

        verify(sleepingService).deleteSleepingById(id);
    }
}
