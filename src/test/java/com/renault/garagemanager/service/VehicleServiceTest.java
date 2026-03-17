package com.renault.garagemanager.service;

import com.renault.garagemanager.dto.GarageDto;
import com.renault.garagemanager.dto.VehicleDto;
import com.renault.garagemanager.repository.GarageRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for Vehicle REST endpoints using REST Assured.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Vehicle REST API")
class VehicleServiceTest {

    private static final String BASE_PATH = "/api/vehicles";

    @LocalServerPort
    private int port;

    @Autowired
    private GarageRepository garageRepository;

    private int garageId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        garageRepository.deleteAll();

        garageId = given()
                .contentType(ContentType.JSON)
                .body(GarageDto.builder()
                        .name("Garage Test")
                        .address("1 rue Test")
                        .telephone("0100000000")
                        .email("test@renault.fr")
                        .build())
        .when()
                .post("/api/garages")
        .then()
                .statusCode(201)
                .extract().path("id");
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private VehicleDto buildClioDto() {
        return VehicleDto.builder()
                .brand("Renault")
                .model("Clio")
                .manufacturingYear(2023)
                .fuelType("Petrol")
                .build();
    }

    private VehicleDto buildMeganeDto() {
        return VehicleDto.builder()
                .brand("Renault")
                .model("Megane")
                .manufacturingYear(2024)
                .fuelType("Diesel")
                .build();
    }

    private int createVehicleAndGetId(VehicleDto dto) {
        return given()
                .contentType(ContentType.JSON)
                .queryParam("garageId", garageId)
                .body(dto)
        .when()
                .post(BASE_PATH)
        .then()
                .statusCode(201)
                .extract().path("id");
    }

    // ── CREATE ───────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("POST /api/vehicles")
    class Create {

        @Test
        @DisplayName("should create vehicle and return 201")
        void shouldReturn201() {
            given()
                    .contentType(ContentType.JSON)
                    .queryParam("garageId", garageId)
                    .body(buildClioDto())
            .when()
                    .post(BASE_PATH)
            .then()
                    .statusCode(201)
                    .body("id", notNullValue())
                    .body("brand", equalTo("Renault"))
                    .body("model", equalTo("Clio"))
                    .body("manufacturingYear", equalTo(2023))
                    .body("fuelType", equalTo("Petrol"))
                    .body("garageId", equalTo(garageId));
        }

        @Test
        @DisplayName("should return 400 when garage is full (50 vehicles)")
        void shouldReturn400_whenGarageIsFull() {
            for (int i = 0; i < 50; i++) {
                createVehicleAndGetId(buildClioDto());
            }

            given()
                    .contentType(ContentType.JSON)
                    .queryParam("garageId", garageId)
                    .body(buildClioDto())
            .when()
                    .post(BASE_PATH)
            .then()
                    .statusCode(400)
                    .body("message", containsString("50"));
        }

        @Test
        @DisplayName("should return 404 when garage does not exist")
        void shouldReturn404_whenGarageNotFound() {
            given()
                    .contentType(ContentType.JSON)
                    .queryParam("garageId", 9999)
                    .body(buildClioDto())
            .when()
                    .post(BASE_PATH)
            .then()
                    .statusCode(404)
                    .body("message", containsString("9999"));
        }
    }

    // ── READ ─────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("GET /api/vehicles")
    class Read {

        @Test
        @DisplayName("should return all vehicles")
        void findAll_shouldReturnAll() {
            createVehicleAndGetId(buildClioDto());
            createVehicleAndGetId(buildMeganeDto());

            given()
            .when()
                    .get(BASE_PATH)
            .then()
                    .statusCode(200)
                    .body("$", hasSize(2));
        }

        @Test
        @DisplayName("/{id} should return vehicle when exists")
        void findById_shouldReturn200() {
            int vehicleId = createVehicleAndGetId(buildClioDto());

            given()
            .when()
                    .get(BASE_PATH + "/{id}", vehicleId)
            .then()
                    .statusCode(200)
                    .body("id", equalTo(vehicleId))
                    .body("brand", equalTo("Renault"))
                    .body("model", equalTo("Clio"));
        }

        @Test
        @DisplayName("/{id} should return 404 when not found")
        void findById_shouldReturn404() {
            given()
            .when()
                    .get(BASE_PATH + "/{id}", 9999)
            .then()
                    .statusCode(404)
                    .body("message", containsString("9999"));
        }

        @Test
        @DisplayName("/garage/{garageId} should return vehicles for garage")
        void findByGarageId_shouldReturnVehicles() {
            createVehicleAndGetId(buildClioDto());

            given()
            .when()
                    .get(BASE_PATH + "/garage/{garageId}", garageId)
            .then()
                    .statusCode(200)
                    .body("$", hasSize(1))
                    .body("[0].brand", equalTo("Renault"));
        }

        @Test
        @DisplayName("/search/by-model should return matching vehicles")
        void findByModel_shouldReturnMatching() {
            createVehicleAndGetId(buildClioDto());
            createVehicleAndGetId(buildMeganeDto());

            given()
                    .queryParam("model", "Clio")
            .when()
                    .get(BASE_PATH + "/search/by-model")
            .then()
                    .statusCode(200)
                    .body("$", hasSize(1))
                    .body("[0].model", equalTo("Clio"));
        }
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("PUT /api/vehicles/{id}")
    class Update {

        @Test
        @DisplayName("should update vehicle and return 200")
        void shouldReturn200() {
            int vehicleId = createVehicleAndGetId(buildClioDto());

            VehicleDto updated = VehicleDto.builder()
                    .brand("Renault")
                    .model("Clio RS")
                    .manufacturingYear(2024)
                    .fuelType("Petrol")
                    .build();

            given()
                    .contentType(ContentType.JSON)
                    .body(updated)
            .when()
                    .put(BASE_PATH + "/{id}", vehicleId)
            .then()
                    .statusCode(200)
                    .body("model", equalTo("Clio RS"))
                    .body("manufacturingYear", equalTo(2024));
        }

        @Test
        @DisplayName("should return 404 when not found")
        void shouldReturn404() {
            given()
                    .contentType(ContentType.JSON)
                    .body(buildClioDto())
            .when()
                    .put(BASE_PATH + "/{id}", 9999)
            .then()
                    .statusCode(404);
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("DELETE /api/vehicles/{id}")
    class Delete {

        @Test
        @DisplayName("should delete vehicle and return 204")
        void shouldReturn204() {
            int vehicleId = createVehicleAndGetId(buildClioDto());

            given()
            .when()
                    .delete(BASE_PATH + "/{id}", vehicleId)
            .then()
                    .statusCode(204);

            // Verify it's gone
            given()
            .when()
                    .get(BASE_PATH + "/{id}", vehicleId)
            .then()
                    .statusCode(404);
        }

        @Test
        @DisplayName("should return 404 when not found")
        void shouldReturn404() {
            given()
            .when()
                    .delete(BASE_PATH + "/{id}", 9999)
            .then()
                    .statusCode(404)
                    .body("message", containsString("9999"));
        }
    }
}
