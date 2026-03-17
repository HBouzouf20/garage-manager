package com.renault.garagemanager.service;

import com.renault.garagemanager.dto.AccessoryDto;
import com.renault.garagemanager.dto.GarageDto;
import com.renault.garagemanager.dto.VehicleDto;
import com.renault.garagemanager.repository.GarageRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for AccessoryController using REST Assured.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AccessoryServiceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private GarageRepository garageRepository;

    private int vehicleId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        garageRepository.deleteAll();

        // Create a garage first
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

        // Create a vehicle in that garage
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

    @Test
    void addAccessoryToVehicle_shouldReturn201() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("vehicleId", vehicleId)
                .body(AccessoryDto.builder()
                        .name("GPS")
                        .description("Navigation system")
                        .price(299.99)
                        .type("Electronics")
                        .build())
                .when()
                .post("/api/accessories")
                .then()
                .statusCode(201)
                .body("name", equalTo("GPS"))
                .body("price", equalTo(299.99F))
                .body("type", equalTo("Electronics"))
                .body("id", notNullValue());
    }

    @Test
    void addAccessoryToVehicle_shouldReturn400_whenNameIsBlank() {
        given()
                .contentType(ContentType.JSON)
                .queryParam("vehicleId", vehicleId)
                .body(AccessoryDto.builder()
                        .name("")
                        .price(99.99)
                        .type("Electronics")
                        .build())
                .when()
                .post("/api/accessories")
                .then()
                .statusCode(400);
    }

    @Test
    void findAccessoriesByVehicleId_shouldReturn200WithList() {
        // Create an accessory first
        given()
                .contentType(ContentType.JSON)
                .queryParam("vehicleId", vehicleId)
                .body(AccessoryDto.builder()
                        .name("GPS")
                        .description("Navigation system")
                        .price(299.99)
                        .type("Electronics")
                        .build())
                .when()
                .post("/api/accessories")
                .then()
                .statusCode(201);

        given()
                .when()
                .get("/api/accessories/vehicle/{vehicleId}", vehicleId)
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].name", equalTo("GPS"));
    }

    @Test
    void findAccessoryById_shouldReturn200() {
        // Create an accessory first
        int accessoryId = given()
                .contentType(ContentType.JSON)
                .queryParam("vehicleId", vehicleId)
                .body(AccessoryDto.builder()
                        .name("GPS")
                        .description("Navigation system")
                        .price(299.99)
                        .type("Electronics")
                        .build())
                .when()
                .post("/api/accessories")
                .then()
                .statusCode(201)
                .extract().path("id");

        given()
                .when()
                .get("/api/accessories/{id}", accessoryId)
                .then()
                .statusCode(200)
                .body("id", equalTo(accessoryId))
                .body("name", equalTo("GPS"));
    }

    @Test
    void findAccessoryById_shouldReturn404_whenNotFound() {
        given()
                .when()
                .get("/api/accessories/{id}", 9999)
                .then()
                .statusCode(404)
                .body("message", containsString("9999"));
    }

    @Test
    void updateAccessory_shouldReturn200() {
        // Create an accessory first
        int accessoryId = given()
                .contentType(ContentType.JSON)
                .queryParam("vehicleId", vehicleId)
                .body(AccessoryDto.builder()
                        .name("GPS")
                        .description("Navigation system")
                        .price(299.99)
                        .type("Electronics")
                        .build())
                .when()
                .post("/api/accessories")
                .then()
                .statusCode(201)
                .extract().path("id");

        given()
                .contentType(ContentType.JSON)
                .body(AccessoryDto.builder()
                        .name("GPS Pro")
                        .description("Advanced navigation")
                        .price(399.99)
                        .type("Electronics")
                        .build())
                .when()
                .put("/api/accessories/{id}", accessoryId)
                .then()
                .statusCode(200)
                .body("name", equalTo("GPS Pro"))
                .body("price", equalTo(399.99F));
    }

    @Test
    void deleteAccessory_shouldReturn204() {
        // Create an accessory first
        int accessoryId = given()
                .contentType(ContentType.JSON)
                .queryParam("vehicleId", vehicleId)
                .body(AccessoryDto.builder()
                        .name("GPS")
                        .description("Navigation system")
                        .price(299.99)
                        .type("Electronics")
                        .build())
                .when()
                .post("/api/accessories")
                .then()
                .statusCode(201)
                .extract().path("id");

        given()
                .when()
                .delete("/api/accessories/{id}", accessoryId)
                .then()
                .statusCode(204);

        // Verify it's gone
        given()
                .when()
                .get("/api/accessories/{id}", accessoryId)
                .then()
                .statusCode(404);
    }

    @Test
    void deleteAccessory_shouldReturn404_whenNotFound() {
        given()
                .when()
                .delete("/api/accessories/{id}", 9999)
                .then()
                .statusCode(404)
                .body("message", containsString("9999"));
    }
}
