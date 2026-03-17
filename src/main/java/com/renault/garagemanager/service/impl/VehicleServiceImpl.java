package com.renault.garagemanager.service.impl;

import com.renault.garagemanager.dto.VehicleDto;
import com.renault.garagemanager.entity.GarageEntity;
import com.renault.garagemanager.entity.VehicleEntity;
import com.renault.garagemanager.exception.BusinessException;
import com.renault.garagemanager.exception.ResourceNotFoundException;
import com.renault.garagemanager.kafka.VehicleProducer;
import com.renault.garagemanager.mapper.VehicleMapper;
import com.renault.garagemanager.repository.GarageRepository;
import com.renault.garagemanager.repository.VehicleRepository;
import com.renault.garagemanager.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service implementation for vehicle management.
 * Enforces the quota constraint of max 50 vehicles per garage.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private static final int MAX_VEHICLES_PER_GARAGE = 50;

    private final VehicleRepository vehicleRepository;
    private final GarageRepository garageRepository;
    private final VehicleMapper vehicleMapper;
    private final VehicleProducer vehicleProducer;

    @Override
    public VehicleDto createVehicle(Long garageId, VehicleDto vehicleDto) {
        if (vehicleRepository.countByGarageId(garageId) >= MAX_VEHICLES_PER_GARAGE) {
            throw new BusinessException("Garage " + garageId + " has reached the maximum capacity of " + MAX_VEHICLES_PER_GARAGE + " vehicles");
        }
        GarageEntity garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + garageId));
        VehicleEntity vehicleEntity = vehicleMapper.toEntity(vehicleDto);
        vehicleEntity.setGarage(garage);
        VehicleDto savedVehicle = vehicleMapper.toDTO(vehicleRepository.save(vehicleEntity));
        vehicleProducer.sendVehicleCreatedEvent(savedVehicle);
        return savedVehicle;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDto> findVehiclesByGarageId(Long garageId) {
        if (!garageRepository.existsById(garageId)) {
            throw new ResourceNotFoundException("Garage not found with id: " + garageId);
        }
        return vehicleRepository.findByGarageId(garageId).stream()
                .map(vehicleMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDto> findAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(vehicleMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDto> findVehiclesByModel(String model) {
        return vehicleRepository.findByModel(model).stream()
                .map(vehicleMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleDto findVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .map(vehicleMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
    }

    @Override
    public VehicleDto updateVehicle(Long id, VehicleDto vehicleDto) {
        VehicleEntity existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        vehicleMapper.updateEntity(existingVehicle, vehicleDto);
        return vehicleMapper.toDTO(existingVehicle);
    }

    @Override
    public void deleteVehicle(Long id) {
        VehicleEntity existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        vehicleRepository.delete(existingVehicle);
    }
}

