package com.renault.garagemanager.service.impl;

import com.renault.garagemanager.dto.AccessoryDto;
import com.renault.garagemanager.entity.AccessoryEntity;
import com.renault.garagemanager.entity.VehicleEntity;
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
 * Service implementation for accessory management.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AccessoryServiceImpl implements AccessoryService {

    private final AccessoryRepository accessoryRepository;
    private final VehicleRepository vehicleRepository;
    private final AccessoryMapper accessoryMapper;

    @Override
    public AccessoryDto createAccessory(Long vehicleId, AccessoryDto accessoryDto) {
        VehicleEntity vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + vehicleId));
        AccessoryEntity accessoryEntity = accessoryMapper.toEntity(accessoryDto);
        accessoryEntity.setVehicle(vehicle);
        return accessoryMapper.toDTO(accessoryRepository.save(accessoryEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccessoryDto> findAccessoriesByVehicleId(Long vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new ResourceNotFoundException("Vehicle not found with id: " + vehicleId);
        }
        return accessoryRepository.findByVehicleId(vehicleId).stream()
                .map(accessoryMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccessoryDto> findAllAccessories() {
        return accessoryRepository.findAll().stream()
                .map(accessoryMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AccessoryDto findAccessoryById(Long id) {
        return accessoryRepository.findById(id)
                .map(accessoryMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Accessory not found with id: " + id));
    }

    @Override
    public AccessoryDto updateAccessory(Long id, AccessoryDto accessoryDto) {
        AccessoryEntity existingAccessory = accessoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Accessory not found with id: " + id));
        accessoryMapper.updateEntity(existingAccessory, accessoryDto);
        return accessoryMapper.toDTO(existingAccessory);
    }

    @Override
    public void deleteAccessory(Long id) {
        AccessoryEntity existingAccessory = accessoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Accessory not found with id: " + id));
        accessoryRepository.delete(existingAccessory);
    }
}

