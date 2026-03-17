package com.renault.garagemanager.service;

import com.renault.garagemanager.dto.AccessoryDto;

import java.util.List;

/**
 * Service contract for accessory management.
 */
public interface AccessoryService {

    AccessoryDto createAccessory(Long vehicleId, AccessoryDto accessoryDto);

    List<AccessoryDto> findAccessoriesByVehicleId(Long vehicleId);

    List<AccessoryDto> findAllAccessories();

    AccessoryDto findAccessoryById(Long id);

    AccessoryDto updateAccessory(Long id, AccessoryDto accessoryDto);

    void deleteAccessory(Long id);
}

