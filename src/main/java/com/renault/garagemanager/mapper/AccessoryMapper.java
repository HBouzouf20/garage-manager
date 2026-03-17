package com.renault.garagemanager.mapper;

import com.renault.garagemanager.dto.AccessoryDto;
import com.renault.garagemanager.entity.AccessoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper between AccessoryEntity and AccessoryDTO.
 */
@Mapper(componentModel = "spring")
public interface AccessoryMapper {

    @Mapping(source = "vehicle.id", target = "vehicleId")
    AccessoryDto toDTO(AccessoryEntity accessory);

    @Mapping(target = "vehicle", ignore = true)
    AccessoryEntity toEntity(AccessoryDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    void updateEntity(@MappingTarget AccessoryEntity accessory, AccessoryDto dto);
}
