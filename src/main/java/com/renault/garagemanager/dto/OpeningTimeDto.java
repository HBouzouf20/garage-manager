package com.renault.garagemanager.dto;

import lombok.Builder;

import java.time.LocalTime;

/**
 * DTO for opening time slots.
 */
@Builder
public record OpeningTimeDto(
        LocalTime startTime,
        LocalTime endTime
) {}

