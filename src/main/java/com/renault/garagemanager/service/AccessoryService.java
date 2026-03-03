package com.renault.garagemanager.service;

import com.renault.garagemanager.dto.AccessoryDTO;

import java.util.List;

/**
 * Contrat de service pour la gestion des accessoires.
 */
public interface AccessoryService {

    AccessoryDTO create(Long vehicleId, AccessoryDTO dto);

    List<AccessoryDTO> findByVehicleId(Long vehicleId);

    AccessoryDTO findById(Long id);

    AccessoryDTO update(Long id, AccessoryDTO dto);

    void delete(Long id);
}

