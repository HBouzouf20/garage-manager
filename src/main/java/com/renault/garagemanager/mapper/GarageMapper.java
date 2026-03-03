package com.renault.garagemanager.mapper;
import com.renault.garagemanager.dto.GarageDTO;
import com.renault.garagemanager.dto.OpeningTimeDTO;
import com.renault.garagemanager.entity.Garage;
import com.renault.garagemanager.entity.OpeningHour;
import com.renault.garagemanager.entity.OpeningTime;
import org.springframework.stereotype.Component;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;
/**
 * Mapper bidirectionnel entre l'entite Garage et son DTO.
 */
@Component
public class GarageMapper {
    public GarageDTO toDTO(Garage garage) {
        Map<DayOfWeek, List<OpeningTimeDTO>> horaires = new HashMap<>();
        if (garage.getHorairesOuverture() != null) {
            horaires = garage.getHorairesOuverture().stream()
                    .collect(Collectors.groupingBy(
                            OpeningHour::getDayOfWeek,
                            Collectors.mapping(
                                    oh -> OpeningTimeDTO.builder()
                                            .startTime(oh.getOpeningTime().getStartTime())
                                            .endTime(oh.getOpeningTime().getEndTime())
                                            .build(),
                                    Collectors.toList()
                            )
                    ));
        }
        return GarageDTO.builder()
                .id(garage.getId())
                .name(garage.getName())
                .address(garage.getAddress())
                .telephone(garage.getTelephone())
                .email(garage.getEmail())
                .horairesOuverture(horaires)
                .build();
    }
    public Garage toEntity(GarageDTO dto) {
        Garage garage = Garage.builder()
                .id(dto.getId())
                .name(dto.getName())
                .address(dto.getAddress())
                .telephone(dto.getTelephone())
                .email(dto.getEmail())
                .build();
        if (dto.getHorairesOuverture() != null) {
            List<OpeningHour> hours = toOpeningHours(dto.getHorairesOuverture(), garage);
            garage.setHorairesOuverture(hours);
        }
        return garage;
    }
    public void updateEntity(Garage garage, GarageDTO dto) {
        garage.setName(dto.getName());
        garage.setAddress(dto.getAddress());
        garage.setTelephone(dto.getTelephone());
        garage.setEmail(dto.getEmail());
        if (dto.getHorairesOuverture() != null) {
            garage.getHorairesOuverture().clear();
            garage.getHorairesOuverture().addAll(toOpeningHours(dto.getHorairesOuverture(), garage));
        }
    }
    private List<OpeningHour> toOpeningHours(Map<DayOfWeek, List<OpeningTimeDTO>> map, Garage garage) {
        List<OpeningHour> hours = new ArrayList<>();
        map.forEach((day, times) ->
                times.forEach(t -> hours.add(OpeningHour.builder()
                        .dayOfWeek(day)
                        .openingTime(OpeningTime.builder()
                                .startTime(t.getStartTime())
                                .endTime(t.getEndTime())
                                .build())
                        .garage(garage)
                        .build()))
        );
        return hours;
    }
}
