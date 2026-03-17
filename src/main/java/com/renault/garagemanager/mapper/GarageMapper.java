package com.renault.garagemanager.mapper;

import com.renault.garagemanager.dto.GarageDto;
import com.renault.garagemanager.dto.OpeningTimeDto;
import com.renault.garagemanager.entity.GarageEntity;
import com.renault.garagemanager.entity.OpeningHourEntity;
import com.renault.garagemanager.entity.OpeningTimeEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MapStruct mapper between GarageEntity and GarageDto.
 */
@Mapper(componentModel = "spring")
public interface GarageMapper {

    default GarageDto toDTO(GarageEntity garage) {
        if (garage == null) return null;

        Map<DayOfWeek, List<OpeningTimeDto>> hours = null;
        if (garage.getOpeningHours() != null) {
            hours = garage.getOpeningHours().stream()
                    .collect(Collectors.groupingBy(
                            OpeningHourEntity::getDayOfWeek,
                            Collectors.mapping(
                                    oh -> new OpeningTimeDto(
                                            oh.getOpeningTime().getStartTime(),
                                            oh.getOpeningTime().getEndTime()),
                                    Collectors.toList()
                            )
                    ));
        }

        return new GarageDto(
                garage.getId(),
                garage.getName(),
                garage.getAddress(),
                garage.getTelephone(),
                garage.getEmail(),
                hours
        );
    }

    @Mapping(target = "openingHours", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    GarageEntity toEntity(GarageDto dto);

    @AfterMapping
    default void mapOpeningHoursToEntity(GarageDto dto, @MappingTarget GarageEntity garage) {
        if (dto.openingHours() == null) return;
        garage.setOpeningHours(toOpeningHours(dto.openingHours(), garage));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "openingHours", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    void updateEntity(@MappingTarget GarageEntity garage, GarageDto dto);

    @AfterMapping
    default void updateOpeningHours(GarageDto dto, @MappingTarget GarageEntity garage) {
        if (dto.openingHours() == null) return;
        garage.getOpeningHours().clear();
        garage.getOpeningHours().addAll(toOpeningHours(dto.openingHours(), garage));
    }

    default List<OpeningHourEntity> toOpeningHours(Map<DayOfWeek, List<OpeningTimeDto>> map, GarageEntity garage) {
        List<OpeningHourEntity> hours = new ArrayList<>();
        map.forEach((day, times) -> times.forEach(t -> hours.add(
                OpeningHourEntity.builder()
                        .dayOfWeek(day)
                        .openingTime(OpeningTimeEntity.builder()
                                .startTime(t.startTime())
                                .endTime(t.endTime())
                                .build())
                        .garage(garage)
                        .build()
        )));
        return hours;
    }
}

