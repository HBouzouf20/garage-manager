package com.renault.garagemanager.service.impl;

import com.renault.garagemanager.dto.VehicleDTO;
import com.renault.garagemanager.entity.Garage;
import com.renault.garagemanager.entity.Vehicle;
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
 * Implementation du service metier pour la gestion des vehicules.
 * Applique la contrainte de quota (max 50 vehicules par garage).
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
    public VehicleDTO create(Long garageId, VehicleDTO dto) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new ResourceNotFoundException("Garage non trouve avec l'id : " + garageId));

        int count = vehicleRepository.countByGarageId(garageId);
        if (count >= MAX_VEHICLES_PER_GARAGE) {
            throw new BusinessException("Le garage a atteint le quota maximum de " + MAX_VEHICLES_PER_GARAGE + " vehicules");
        }

        Vehicle vehicle = vehicleMapper.toEntity(dto);
        vehicle.setGarage(garage);
        Vehicle saved = vehicleRepository.save(vehicle);
        VehicleDTO result = vehicleMapper.toDTO(saved);
        vehicleProducer.sendVehicleCreatedEvent(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> findByGarageId(Long garageId) {
        if (!garageRepository.existsById(garageId)) {
            throw new ResourceNotFoundException("Garage non trouve avec l'id : " + garageId);
        }
        return vehicleRepository.findByGarageId(garageId).stream()
                .map(vehicleMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> findByModel(String model) {
        return vehicleRepository.findByModel(model).stream()
                .map(vehicleMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleDTO findById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule non trouve avec l'id : " + id));
        return vehicleMapper.toDTO(vehicle);
    }

    @Override
    public VehicleDTO update(Long id, VehicleDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule non trouve avec l'id : " + id));
        vehicleMapper.updateEntity(vehicle, dto);
        return vehicleMapper.toDTO(vehicleRepository.save(vehicle));
    }

    @Override
    public void delete(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicule non trouve avec l'id : " + id);
        }
        vehicleRepository.deleteById(id);
    }
}

