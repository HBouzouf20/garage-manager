package com.renault.garagemanager.service;

import com.renault.garagemanager.dto.GarageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service contract for garage management.
 */
public interface GarageService {

    GarageDto createGarage(GarageDto garageDto);

    GarageDto findGarageById(Long id);

    Page<GarageDto> findAllGarages(Pageable pageable);

    GarageDto updateGarage(Long id, GarageDto garageDto);

    void deleteGarage(Long id);

    List<GarageDto> findGaragesByVehicleType(String fuelType);

    List<GarageDto> findGaragesByAccessoryName(String accessoryName);
}

