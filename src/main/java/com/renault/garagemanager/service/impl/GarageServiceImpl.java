package com.renault.garagemanager.service.impl;

import com.renault.garagemanager.dto.GarageDto;
import com.renault.garagemanager.entity.GarageEntity;
import com.renault.garagemanager.exception.ResourceNotFoundException;
import com.renault.garagemanager.mapper.GarageMapper;
import com.renault.garagemanager.repository.GarageRepository;
import com.renault.garagemanager.service.GarageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service implementation for garage management.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class GarageServiceImpl implements GarageService {

    private final GarageRepository garageRepository;
    private final GarageMapper garageMapper;

    @Override
    public GarageDto createGarage(GarageDto garageDto) {
        GarageEntity garageEntity = garageMapper.toEntity(garageDto);
        GarageEntity savedGarage = garageRepository.save(garageEntity);
        return garageMapper.toDTO(savedGarage);
    }

    @Override
    @Transactional(readOnly = true)
    public GarageDto findGarageById(Long id) {
        return garageRepository.findById(id)
                .map(garageMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GarageDto> findAllGarages(Pageable pageable) {
        return garageRepository.findAll(pageable).map(garageMapper::toDTO);
    }

    @Override
    public GarageDto updateGarage(Long id, GarageDto garageDto) {
        GarageEntity existingGarage = garageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + id));
        garageMapper.updateEntity(existingGarage, garageDto);
        return garageMapper.toDTO(existingGarage);
    }

    @Override
    public void deleteGarage(Long id) {
        GarageEntity existingGarage = garageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + id));
        garageRepository.delete(existingGarage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GarageDto> findGaragesByVehicleType(String fuelType) {
        return garageRepository.findByVehicleType(fuelType).stream()
                .map(garageMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GarageDto> findGaragesByAccessoryName(String accessoryName) {
        return garageRepository.findByAccessoryName(accessoryName).stream()
                .map(garageMapper::toDTO)
                .toList();
    }
}

