package com.renault.garagemanager.service;

import com.renault.garagemanager.dto.AccessoryDto;
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
 * REST Assured integration tests for the Accessory API ({@code /api/accessories}).
 * Runs a full Spring Boot context on a random port against an in-memory H2 database.
 * A garage and a vehicle are created before each test to satisfy the accessory–vehicle relationship.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Accessory REST API")
class AccessoryServiceTest {

    private static final String BASE_PATH = "/api/accessories";

    @LocalServerPort
    private int port;

    @Autowired
    private GarageRepository garageRepository;

    private int vehicleId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        garageRepository.deleteAll();

        int garageId = given()
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

        vehicleId = given()
                .contentType(ContentType.JSON)
                .queryParam("garageId", garageId)
                .body(VehicleDto.builder()
                        .brand("Renault")
                        .model("Clio")
                        .manufacturingYear(2023)
                        .fuelType("Petrol")
                        .build())
                .when()
                .post("/api/vehicles")
                .then()
                .statusCode(201)
                .extract().path("id");
    }

    private AccessoryDto buildGpsDto() {
        return AccessoryDto.builder()
                .name("GPS")
                .description("Navigation system")
                .price(299.99)
                .type("Electronics")
                .build();
    }

    private int createAccessoryAndGetId(AccessoryDto dto) {
        return given()
                .contentType(ContentType.JSON)
                .queryParam("vehicleId", vehicleId)
                .body(dto)
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(201)
                .extract().path("id");
    }

    @Nested
    @DisplayName("POST /api/accessories")
    class Create {

        @Test
        @DisplayName("should create accessory and return 201")
        void shouldReturn201() {
            given()
                    .contentType(ContentType.JSON)
                    .queryParam("vehicleId", vehicleId)
                    .body(buildGpsDto())
                    .when()
                    .post(BASE_PATH)
                    .then()
                    .statusCode(201)
                    .body("id", notNullValue())
                    .body("name", equalTo("GPS"))
                    .body("price", equalTo(299.99F))
                    .body("type", equalTo("Electronics"));
        }

        @Test
        @DisplayName("should return 400 when name is blank")
        void shouldReturn400_whenNameIsBlank() {
            given()
                    .contentType(ContentType.JSON)
                    .queryParam("vehicleId", vehicleId)
                    .body(AccessoryDto.builder()
                            .name("")
                            .price(99.99)
                            .type("Electronics")
                            .build())
                    .when()
                    .post(BASE_PATH)
                    .then()
                    .statusCode(400);
        }
    }

    @Nested
    @DisplayName("GET /api/accessories")
    class Read {

        @Test
        @DisplayName("/vehicle/{vehicleId} should return accessories for the vehicle")
        void findAccessoriesByVehicleId_shouldReturn200WithList() {
            createAccessoryAndGetId(buildGpsDto());

            given()
                    .when()
                    .get(BASE_PATH + "/vehicle/{vehicleId}", vehicleId)
                    .then()
                    .statusCode(200)
                    .body("$", hasSize(1))
                    .body("[0].name", equalTo("GPS"));
        }

        @Test
        @DisplayName("/{id} should return accessory when exists")
        void findById_shouldReturn200() {
            int accessoryId = createAccessoryAndGetId(buildGpsDto());

            given()
                    .when()
                    .get(BASE_PATH + "/{id}", accessoryId)
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(accessoryId))
                    .body("name", equalTo("GPS"));
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
    }

    @Nested
    @DisplayName("PUT /api/accessories/{id}")
    class Update {

        @Test
        @DisplayName("should update accessory and return 200")
        void shouldReturn200() {
            int accessoryId = createAccessoryAndGetId(buildGpsDto());

            given()
                    .contentType(ContentType.JSON)
                    .body(AccessoryDto.builder()
                            .name("GPS Pro")
                            .description("Advanced navigation")
                            .price(399.99)
                            .type("Electronics")
                            .build())
                    .when()
                    .put(BASE_PATH + "/{id}", accessoryId)
                    .then()
                    .statusCode(200)
                    .body("name", equalTo("GPS Pro"))
                    .body("price", equalTo(399.99F));
        }
    }

    @Nested
    @DisplayName("DELETE /api/accessories/{id}")
    class Delete {

        @Test
        @DisplayName("should delete accessory and return 204")
        void shouldReturn204() {
            int accessoryId = createAccessoryAndGetId(buildGpsDto());

            given()
                    .when()
                    .delete(BASE_PATH + "/{id}", accessoryId)
                    .then()
                    .statusCode(204);

            given()
                    .when()
                    .get(BASE_PATH + "/{id}", accessoryId)
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
