package com.example.babee_transmission_project.controller;

import com.example.babee_transmission_project.model.ActivityResource;
import com.example.babee_transmission_project.model.input.ActivityInputResource;
import com.example.babee_transmission_project.service.ActivityService;
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


@WebMvcTest(ActivityController.class)
class ActivityControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityService activityService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetActivities() throws Exception {
        ActivityResource activity = ActivityResource.builder()
                .id(UUID.randomUUID())
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .name("Activity Name")
                .commentaire("Test commentaire")
                .build();

        when(activityService.getActivities(any(), any())).thenReturn(List.of(activity));

        mockMvc.perform(get("/activity")
                        .param("babeeId", UUID.randomUUID().toString())
                        .param("day", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Activity Name"))
                .andExpect(jsonPath("$[0].commentaire").value("Test commentaire"));

        verify(activityService).getActivities(any(), any());
    }

    @Test
    void shouldGetActivityById() throws Exception {
        UUID id = UUID.randomUUID();
        ActivityResource activity = ActivityResource.builder()
                .id(id)
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .name("Test Name")
                .commentaire("Test Commentaire")
                .build();

        when(activityService.getActivityById(id)).thenReturn(activity);

        mockMvc.perform(get("/activity/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Name"))
                .andExpect(jsonPath("$.commentaire").value("Test Commentaire"));

        verify(activityService).getActivityById(id);
    }

    @Test
    void shouldSaveActivity() throws Exception {
        ActivityInputResource input = ActivityInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .name("New Activity")
                .commentaire("New Comment")
                .build();

        ActivityResource saved = ActivityResource.builder()
                .id(UUID.randomUUID())
                .babeeId(input.getBabeeId())
                .eventDate(input.getEventDate())
                .name(input.getName())
                .commentaire(input.getCommentaire())
                .build();

        when(activityService.saveActivity(any())).thenReturn(saved);

        mockMvc.perform(post("/activity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Activity"))
                .andExpect(jsonPath("$.commentaire").value("New Comment"));

        verify(activityService).saveActivity(any());
    }

    @Test
    void shouldFailSavingActivityWhenBabeeIdIsNull() throws Exception {
        ActivityInputResource input = ActivityInputResource.builder()
                .eventDate(LocalDate.now())
                .name("New Activity")
                .commentaire("New Comment")
                .build();

        mockMvc.perform(post("/activity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.babeeId").value("must not be null"));
    }

    @Test
    void shouldFailSavingActivityWhenEventDateIsNull() throws Exception {
        ActivityInputResource input = ActivityInputResource.builder()
                .babeeId(UUID.randomUUID())
                .name("New Activity")
                .commentaire("New Comment")
                .build();

        mockMvc.perform(post("/activity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.eventDate").value("must not be null"));
    }

    @Test
    void shouldFailSavingActivityWhenNameIsNull() throws Exception {
        ActivityInputResource input = ActivityInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .commentaire("New Comment")
                .build();

        mockMvc.perform(post("/activity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("must not be null"));
    }

    @Test
    void shouldUpdateActivity() throws Exception {
        UUID id = UUID.randomUUID();
        ActivityInputResource input = ActivityInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .name("Updated Activity")
                .commentaire("Updated Comment")
                .build();

        ActivityResource updated = ActivityResource.builder()
                .id(id)
                .babeeId(input.getBabeeId())
                .eventDate(input.getEventDate())
                .name(input.getName())
                .commentaire(input.getCommentaire())
                .build();

        when(activityService.updateActivity(eq(id), any())).thenReturn(updated);

        mockMvc.perform(put("/activity/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Activity"))
                .andExpect(jsonPath("$.commentaire").value("Updated Comment"));

        verify(activityService).updateActivity(eq(id), any());
    }

    @Test
    void shouldFailUpdatingActivityWhenBabeeIdIsNull() throws Exception {
        UUID id = UUID.randomUUID();
        ActivityInputResource input = ActivityInputResource.builder()
                .eventDate(LocalDate.now())
                .name("Updated Activity")
                .commentaire("Updated Comment")
                .build();

        mockMvc.perform(put("/activity/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.babeeId").value("must not be null"));
    }

    @Test
    void shouldFailUpdatingActivityWhenEventDateIsNull() throws Exception {
        UUID id = UUID.randomUUID();
        ActivityInputResource input = ActivityInputResource.builder()
                .babeeId(UUID.randomUUID())
                .name("Updated Activity")
                .commentaire("Updated Comment")
                .build();

        mockMvc.perform(put("/activity/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.eventDate").value("must not be null"));
    }

    @Test
    void shouldFailUpdatingActivityWhenNameIsNull() throws Exception {
        UUID id = UUID.randomUUID();
        ActivityInputResource input = ActivityInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .commentaire("Updated Comment")
                .build();

        mockMvc.perform(put("/activity/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("must not be null"));
    }

    @Test
    void shouldDeleteActivityById() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/activity/{id}", id))
                .andExpect(status().isOk());

        verify(activityService).deleteActivityById(id);
    }
}
