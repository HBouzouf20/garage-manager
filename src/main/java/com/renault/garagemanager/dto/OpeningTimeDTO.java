package com.renault.garagemanager.dto;
import lombok.*;
import java.time.LocalTime;
/**
 * DTO pour les creneaux horaires d'ouverture.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpeningTimeDTO {
    private LocalTime startTime;
    private LocalTime endTime;
}
