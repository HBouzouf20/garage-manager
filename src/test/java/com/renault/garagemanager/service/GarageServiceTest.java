package com.renault.garagemanager.service;

import com.renault.garagemanager.dto.GarageDto;
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
 * REST Assured integration tests for the Garage API ({@code /api/garages}).
 * Runs a full Spring Boot context on a random port against an in-memory H2 database.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Garage REST API")
class GarageServiceTest {

    private static final String BASE_PATH = "/api/garages";

    @LocalServerPort
    private int port;

    @Autowired
    private GarageRepository garageRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = BASE_PATH;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        garageRepository.deleteAll();
    }

    private GarageDto buildParisGarage() {
        return GarageDto.builder()
                .name("Garage Renault Paris")
                .address("10 rue de la Paix, Paris")
                .telephone("0145678900")
                .email("paris@renault.fr")
                .build();
    }

    private int createGarageAndGetId(GarageDto dto) {
        return given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract().path("id");
    }

    @Nested
    @DisplayName("POST /api/garages")
    class Create {

        @Test
        @DisplayName("should create garage and return 201")
        void shouldReturn201() {
            given()
                    .contentType(ContentType.JSON)
                    .body(buildParisGarage())
                    .when()
                    .post()
                    .then()
                    .statusCode(201)
                    .body("id", notNullValue())
                    .body("name", equalTo("Garage Renault Paris"))
                    .body("address", equalTo("10 rue de la Paix, Paris"))
                    .body("email", equalTo("paris@renault.fr"));
        }

        @Test
        @DisplayName("should return 400 when name is blank")
        void shouldReturn400_whenNameIsBlank() {
            GarageDto invalid = GarageDto.builder()
                    .name("")
                    .address("addr")
                    .telephone("01234")
                    .email("x@x.fr")
                    .build();

            given()
                    .contentType(ContentType.JSON)
                    .body(invalid)
                    .when()
                    .post()
                    .then()
                    .statusCode(400);
        }
    }

    @Nested
    @DisplayName("GET /api/garages")
    class Read {

        @Test
        @DisplayName("should return paged results")
        void findAll_shouldReturnPagedResults() {
            createGarageAndGetId(buildParisGarage());

            given()
                    .when()
                    .get()
                    .then()
                    .statusCode(200)
                    .body("totalElements", equalTo(1))
                    .body("content[0].name", equalTo("Garage Renault Paris"));
        }

        @Test
        @DisplayName("/{id} should return garage when exists")
        void findById_shouldReturn200() {
            int id = createGarageAndGetId(buildParisGarage());

            given()
                    .when()
                    .get("/{id}", id)
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(id))
                    .body("name", equalTo("Garage Renault Paris"));
        }

        @Test
        @DisplayName("/{id} should return 404 when not found")
        void findById_shouldReturn404() {
            given()
                    .when()
                    .get("/{id}", 9999)
                    .then()
                    .statusCode(404)
                    .body("message", containsString("9999"));
        }
    }

    @Nested
    @DisplayName("PUT /api/garages/{id}")
    class Update {

        @Test
        @DisplayName("should update garage and return 200")
        void shouldReturn200() {
            int id = createGarageAndGetId(buildParisGarage());

            GarageDto updated = GarageDto.builder()
                    .name("Garage Renault Lyon")
                    .address("5 place Bellecour, Lyon")
                    .telephone("0478901234")
                    .email("lyon@renault.fr")
                    .build();

            given()
                    .contentType(ContentType.JSON)
                    .body(updated)
                    .when()
                    .put("/{id}", id)
                    .then()
                    .statusCode(200)
                    .body("name", equalTo("Garage Renault Lyon"))
                    .body("address", equalTo("5 place Bellecour, Lyon"));
        }

        @Test
        @DisplayName("should return 404 when not found")
        void shouldReturn404() {
            given()
                    .contentType(ContentType.JSON)
                    .body(buildParisGarage())
                    .when()
                    .put("/{id}", 9999)
                    .then()
                    .statusCode(404);
        }
    }

    @Nested
    @DisplayName("DELETE /api/garages/{id}")
    class Delete {

        @Test
        @DisplayName("should delete garage and return 204")
        void shouldReturn204() {
            int id = createGarageAndGetId(buildParisGarage());

            given()
                    .when()
                    .delete("/{id}", id)
                    .then()
                    .statusCode(204);

            given()
                    .when()
                    .get("/{id}", id)
                    .then()
                    .statusCode(404);
        }

        @Test
        @DisplayName("should return 404 when not found")
        void shouldReturn404() {
            given()
                    .when()
                    .delete("/{id}", 9999)
                    .then()
                    .statusCode(404)
                    .body("message", containsString("9999"));
        }
    }
}