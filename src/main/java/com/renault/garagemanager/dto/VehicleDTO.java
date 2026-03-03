package com.renault.garagemanager.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
/**
 * DTO pour la creation et modification d'un vehicule.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDTO {
    private Long id;
    @NotBlank
    private String brand;
    @NotBlank
    private String model;
    @NotNull
    @Positive
    private Integer anneeFabrication;
    @NotBlank
    private String typeCarburant;
    private Long garageId;
}
