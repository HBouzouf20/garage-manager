package com.renault.garagemanager.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
/**
 * DTO pour la creation et modification d'un accessoire.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessoryDTO {
    private Long id;
    @NotBlank
    private String nom;
    private String description;
    @Positive
    private double prix;
    @NotBlank
    private String type;
    private Long vehicleId;
}
