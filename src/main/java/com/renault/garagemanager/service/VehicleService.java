package com.renault.garagemanager.service;

import com.renault.garagemanager.dto.VehicleDto;

import java.util.List;

/**
 * Service contract for vehicle management.
 */
public interface VehicleService {

    VehicleDto createVehicle(Long garageId, VehicleDto vehicleDto);

    List<VehicleDto> findVehiclesByGarageId(Long garageId);

    List<VehicleDto> findAllVehicles();

    List<VehicleDto> findVehiclesByModel(String model);

    VehicleDto findVehicleById(Long id);

    VehicleDto updateVehicle(Long id, VehicleDto vehicleDto);

    void deleteVehicle(Long id);
}

