package com.renault.garagemanager.service.impl;

import com.renault.garagemanager.dto.GarageDTO;
import com.renault.garagemanager.entity.Garage;
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
 * Implementation du service metier pour la gestion des garages.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class GarageServiceImpl implements GarageService {

    private final GarageRepository garageRepository;
    private final GarageMapper garageMapper;

    @Override
    public GarageDTO create(GarageDTO dto) {
        Garage garage = garageMapper.toEntity(dto);
        return garageMapper.toDTO(garageRepository.save(garage));
    }

    @Override
    @Transactional(readOnly = true)
    public GarageDTO findById(Long id) {
        Garage garage = garageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Garage non trouve avec l'id : " + id));
        return garageMapper.toDTO(garage);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GarageDTO> findAll(Pageable pageable) {
        return garageRepository.findAll(pageable).map(garageMapper::toDTO);
    }

    @Override
    public GarageDTO update(Long id, GarageDTO dto) {
        Garage garage = garageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Garage non trouve avec l'id : " + id));
        garageMapper.updateEntity(garage, dto);
        return garageMapper.toDTO(garageRepository.save(garage));
    }

    @Override
    public void delete(Long id) {
        if (!garageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Garage non trouve avec l'id : " + id);
        }
        garageRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GarageDTO> findByVehicleType(String typeCarburant) {
        return garageRepository.findByVehicleType(typeCarburant).stream()
                .map(garageMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GarageDTO> findByAccessoryName(String accessoryName) {
        return garageRepository.findByAccessoryName(accessoryName).stream()
                .map(garageMapper::toDTO)
                .toList();
    }
}

