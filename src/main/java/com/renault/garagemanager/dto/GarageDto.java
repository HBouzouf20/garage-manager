package com.renault.garagemanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

/**
 * DTO for creating and updating a garage.
 */
@Builder
public record GarageDto(
        Long id,
        @NotBlank String name,
        @NotBlank String address,
        @NotBlank String telephone,
        @NotBlank @Email String email,
        Map<DayOfWeek, List<OpeningTimeDto>> openingHours
) {}

