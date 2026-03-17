package com.renault.garagemanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

/**
 * DTO for creating and updating a vehicle.
 */
@Builder
public record VehicleDto(
        Long id,
        @NotBlank String brand,
        @NotBlank String model,
        @NotNull @Positive Integer manufacturingYear,
        @NotBlank String fuelType,
        Long garageId
) {}

