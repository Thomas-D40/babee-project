package com.example.babee_transmission_project.controller;

import com.example.babee_transmission_project.model.CareActResource;
import com.example.babee_transmission_project.model.input.CareActInputResource;
import com.example.babee_transmission_project.service.CareActService;
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

@WebMvcTest(CareActController.class)
class CareActControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CareActService careActService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetCareActs() throws Exception {
        CareActResource careAct = CareActResource.builder()
                .id(UUID.randomUUID())
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .careActType(1)
                .careActDetail(2)
                .comment("Test commentaire")
                .build();

        when(careActService.getCareActs(any(), any())).thenReturn(List.of(careAct));

        mockMvc.perform(get("/care-act")
                        .param("babeeId", UUID.randomUUID().toString())
                        .param("day", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].careActType").value(1))
                .andExpect(jsonPath("$[0].careActDetail").value(2))
                .andExpect(jsonPath("$[0].comment").value("Test commentaire"));

        verify(careActService).getCareActs(any(), any());
    }

    @Test
    void shouldGetCareActById() throws Exception {
        UUID id = UUID.randomUUID();
        CareActResource careAct = CareActResource.builder()
                .id(id)
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .careActType(1)
                .careActDetail(2)
                .comment("Test Commentaire")
                .build();

        when(careActService.getCareActById(id)).thenReturn(careAct);

        mockMvc.perform(get("/care-act/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.careActType").value(1))
                .andExpect(jsonPath("$.careActDetail").value(2))
                .andExpect(jsonPath("$.comment").value("Test Commentaire"));

        verify(careActService).getCareActById(id);
    }

    @Test
    void shouldSaveCareAct() throws Exception {
        CareActInputResource input = CareActInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .careActType(1)
                .careActDetail(2)
                .comment("New Comment")
                .build();

        CareActResource saved = CareActResource.builder()
                .id(UUID.randomUUID())
                .babeeId(input.getBabeeId())
                .eventDate(input.getEventDate())
                .careActType(input.getCareActType())
                .careActDetail(input.getCareActDetail())
                .comment(input.getComment())
                .build();

        when(careActService.saveCareAct(any())).thenReturn(saved);

        mockMvc.perform(post("/care-act")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.careActType").value(1))
                .andExpect(jsonPath("$.careActDetail").value(2))
                .andExpect(jsonPath("$.comment").value("New Comment"));

        verify(careActService).saveCareAct(any());
    }

    @Test
    void shouldFailSavingCareActWhenBabeeIdIsNull() throws Exception {
        CareActInputResource input = CareActInputResource.builder()
                .eventDate(LocalDate.now())
                .careActType(1)
                .careActDetail(2)
                .comment("New Comment")
                .build();

        mockMvc.perform(post("/care-act")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.babeeId").value("must not be null"));
    }

    @Test
    void shouldFailSavingCareActWhenEventDateIsNull() throws Exception {
        CareActInputResource input = CareActInputResource.builder()
                .babeeId(UUID.randomUUID())
                .careActType(1)
                .careActDetail(2)
                .comment("New Comment")
                .build();

        mockMvc.perform(post("/care-act")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.eventDate").value("must not be null"));
    }

    @Test
    void shouldFailSavingCareActWhenCareActTypeIsNull() throws Exception {
        CareActInputResource input = CareActInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .careActDetail(2)
                .comment("New Comment")
                .build();

        mockMvc.perform(post("/care-act")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.careActType").value("must not be null"));
    }

    @Test
    void shouldUpdateCareAct() throws Exception {
        UUID id = UUID.randomUUID();
        CareActInputResource input = CareActInputResource.builder()
                .babeeId(UUID.randomUUID())
                .eventDate(LocalDate.now())
                .careActType(1)
                .careActDetail(2)
                .comment("Updated Comment")
                .build();

        CareActResource updated = CareActResource.builder()
                .id(id)
                .babeeId(input.getBabeeId())
                .eventDate(input.getEventDate())
                .careActType(input.getCareActType())
                .careActDetail(input.getCareActDetail())
                .comment(input.getComment())
                .build();

        when(careActService.updateCareAct(eq(id), any())).thenReturn(updated);

        mockMvc.perform(put("/care-act/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.careActType").value(1))
                .andExpect(jsonPath("$.careActDetail").value(2))
                .andExpect(jsonPath("$.comment").value("Updated Comment"));

        verify(careActService).updateCareAct(eq(id), any());
    }

    @Test
    void shouldDeleteCareActById() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/care-act/{id}", id))
                .andExpect(status().isOk());

        verify(careActService).deleteCareActById(id);
    }
}
