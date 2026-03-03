package com.renault.garagemanager.controller;

import tools.jackson.databind.ObjectMapper;
import com.renault.garagemanager.dto.GarageDTO;
import com.renault.garagemanager.service.GarageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'integration du controleur GarageController.
 */
@WebMvcTest(GarageController.class)
class GarageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GarageService garageService;

    @Test
    void create_shouldReturn201() throws Exception {
        GarageDTO dto = GarageDTO.builder()
                .name("Garage Renault Lyon")
                .address("5 place Bellecour, Lyon")
                .telephone("0478901234")
                .email("lyon@renault.fr")
                .build();

        GarageDTO saved = GarageDTO.builder()
                .id(1L)
                .name("Garage Renault Lyon")
                .address("5 place Bellecour, Lyon")
                .telephone("0478901234")
                .email("lyon@renault.fr")
                .build();

        when(garageService.create(any(GarageDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/api/garages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Garage Renault Lyon"));
    }

    @Test
    void create_shouldReturn400_whenNameIsBlank() throws Exception {
        GarageDTO dto = GarageDTO.builder()
                .name("")
                .address("5 place Bellecour")
                .telephone("0478901234")
                .email("lyon@renault.fr")
                .build();

        mockMvc.perform(post("/api/garages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findById_shouldReturn200() throws Exception {
        GarageDTO dto = GarageDTO.builder()
                .id(1L)
                .name("Garage Renault Lyon")
                .address("5 place Bellecour, Lyon")
                .telephone("0478901234")
                .email("lyon@renault.fr")
                .build();

        when(garageService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/garages/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Garage Renault Lyon"));
    }

    @Test
    void findAll_shouldReturnPagedResults() throws Exception {
        GarageDTO dto = GarageDTO.builder()
                .id(1L)
                .name("Garage Renault Lyon")
                .build();

        when(garageService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(dto)));

        mockMvc.perform(get("/api/garages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Garage Renault Lyon"));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/garages/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void searchByVehicleType_shouldReturn200() throws Exception {
        GarageDTO dto = GarageDTO.builder().id(1L).name("Garage Diesel").build();
        when(garageService.findByVehicleType("Diesel")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/garages/search/by-vehicle-type")
                        .param("typeCarburant", "Diesel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Garage Diesel"));
    }
}

