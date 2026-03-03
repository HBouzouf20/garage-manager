package com.renault.garagemanager.service;

import com.renault.garagemanager.dto.GarageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Contrat de service pour la gestion des garages.
 */
public interface GarageService {

    GarageDTO create(GarageDTO dto);

    GarageDTO findById(Long id);

    Page<GarageDTO> findAll(Pageable pageable);

    GarageDTO update(Long id, GarageDTO dto);

    void delete(Long id);

    List<GarageDTO> findByVehicleType(String typeCarburant);

    List<GarageDTO> findByAccessoryName(String accessoryName);
}

