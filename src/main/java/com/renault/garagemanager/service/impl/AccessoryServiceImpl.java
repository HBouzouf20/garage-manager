package com.renault.garagemanager.service.impl;

import com.renault.garagemanager.dto.AccessoryDTO;
import com.renault.garagemanager.entity.Accessory;
import com.renault.garagemanager.entity.Vehicle;
import com.renault.garagemanager.exception.ResourceNotFoundException;
import com.renault.garagemanager.mapper.AccessoryMapper;
import com.renault.garagemanager.repository.AccessoryRepository;
import com.renault.garagemanager.repository.VehicleRepository;
import com.renault.garagemanager.service.AccessoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation du service metier pour la gestion des accessoires.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AccessoryServiceImpl implements AccessoryService {

    private final AccessoryRepository accessoryRepository;
    private final VehicleRepository vehicleRepository;
    private final AccessoryMapper accessoryMapper;

    @Override
    public AccessoryDTO create(Long vehicleId, AccessoryDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule non trouve avec l'id : " + vehicleId));
        Accessory accessory = accessoryMapper.toEntity(dto);
        accessory.setVehicle(vehicle);
        return accessoryMapper.toDTO(accessoryRepository.save(accessory));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccessoryDTO> findByVehicleId(Long vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new ResourceNotFoundException("Vehicule non trouve avec l'id : " + vehicleId);
        }
        return accessoryRepository.findByVehicleId(vehicleId).stream()
                .map(accessoryMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AccessoryDTO findById(Long id) {
        Accessory accessory = accessoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Accessoire non trouve avec l'id : " + id));
        return accessoryMapper.toDTO(accessory);
    }

    @Override
    public AccessoryDTO update(Long id, AccessoryDTO dto) {
        Accessory accessory = accessoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Accessoire non trouve avec l'id : " + id));
        accessoryMapper.updateEntity(accessory, dto);
        return accessoryMapper.toDTO(accessoryRepository.save(accessory));
    }

    @Override
    public void delete(Long id) {
        if (!accessoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Accessoire non trouve avec l'id : " + id);
        }
        accessoryRepository.deleteById(id);
    }
}

