package com.renault.garagemanager.controller;

import com.renault.garagemanager.dto.GarageDto;
import com.renault.garagemanager.service.GarageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for {@link GarageController} using the Spring MVC test slice.
 * The service layer is replaced by a Mockito stub so no database or Kafka broker is required.
 */
@WebMvcTest(GarageController.class)
@ActiveProfiles("test")
@DisplayName("GarageController")
class GarageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GarageService garageService;

    private GarageDto parisGarageDto;

    @BeforeEach
    void setUp() {
        parisGarageDto = GarageDto.builder()
                .id(1L)
                .name("Garage Renault Paris")
                .address("10 rue de la Paix, Paris")
                .telephone("0145678900")
                .email("paris@renault.fr")
                .build();
    }

    @Nested
    @DisplayName("GET /api/garages")
    class FindAll {

        @Test
        @DisplayName("should return paged results with 200")
        void findAll_shouldReturnPagedResults() throws Exception {
            when(garageService.findAllGarages(any()))
                    .thenReturn(new PageImpl<>(List.of(parisGarageDto), PageRequest.of(0, 10), 1));

            mockMvc.perform(get("/api/garages"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].name").value("Garage Renault Paris"))
                    .andExpect(jsonPath("$.totalElements").value(1));
        }
    }

    @Nested
    @DisplayName("GET /api/garages/search/by-vehicle-type")
    class SearchByVehicleType {

        @Test
        @DisplayName("should return matching garages with 200")
        void searchByVehicleType_shouldReturn200() throws Exception {
            when(garageService.findGaragesByVehicleType("Electric"))
                    .thenReturn(List.of(parisGarageDto));

            mockMvc.perform(get("/api/garages/search/by-vehicle-type")
                            .param("fuelType", "Electric")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("Garage Renault Paris"));
        }
    }
}
