package com.renault.garagemanager.mapper;

import com.renault.garagemanager.dto.VehicleDto;
import com.renault.garagemanager.entity.VehicleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper between VehicleEntity and VehicleDto.
 */
@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mapping(source = "garage.id", target = "garageId")
    VehicleDto toDTO(VehicleEntity vehicle);

    @Mapping(target = "garage", ignore = true)
    @Mapping(target = "accessories", ignore = true)
    VehicleEntity toEntity(VehicleDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "garage", ignore = true)
    @Mapping(target = "accessories", ignore = true)
    void updateEntity(@MappingTarget VehicleEntity vehicle, VehicleDto dto);
}
