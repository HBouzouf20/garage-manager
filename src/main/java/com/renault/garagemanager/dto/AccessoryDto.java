package com.renault.garagemanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

/**
 * DTO for creating and updating an accessory.
 */
@Builder
public record AccessoryDto(
        Long id,
        @NotBlank String name,
        String description,
        @Positive double price,
        @NotBlank String type,
        Long vehicleId
) {}

