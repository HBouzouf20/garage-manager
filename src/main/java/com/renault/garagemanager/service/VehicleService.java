package com.renault.garagemanager.service;

import com.renault.garagemanager.dto.VehicleDTO;

import java.util.List;

/**
 * Contrat de service pour la gestion des vehicules.
 */
public interface VehicleService {

    VehicleDTO create(Long garageId, VehicleDTO dto);

    List<VehicleDTO> findByGarageId(Long garageId);

    List<VehicleDTO> findByModel(String model);

    VehicleDTO findById(Long id);

    VehicleDTO update(Long id, VehicleDTO dto);

    void delete(Long id);
}

