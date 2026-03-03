package com.renault.garagemanager.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
/**
 * DTO pour la creation et modification d'un garage.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarageDTO {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @NotBlank
    private String telephone;
    @NotBlank
    @Email
    private String email;
    private Map<DayOfWeek, List<OpeningTimeDTO>> horairesOuverture;
}
