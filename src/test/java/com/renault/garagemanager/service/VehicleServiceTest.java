package com.renault.garagemanager.service;

import com.renault.garagemanager.dto.VehicleDTO;
import com.renault.garagemanager.entity.Garage;
import com.renault.garagemanager.entity.Vehicle;
import com.renault.garagemanager.exception.BusinessException;
import com.renault.garagemanager.exception.ResourceNotFoundException;
import com.renault.garagemanager.kafka.VehicleProducer;
import com.renault.garagemanager.mapper.VehicleMapper;
import com.renault.garagemanager.repository.GarageRepository;
import com.renault.garagemanager.repository.VehicleRepository;
import com.renault.garagemanager.service.impl.VehicleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires du service VehicleService.
 */
@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private VehicleProducer vehicleProducer;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private Garage garage;
    private Vehicle vehicle;
    private VehicleDTO vehicleDTO;

    @BeforeEach
    void setUp() {
        garage = Garage.builder().id(1L).name("Garage Test").build();

        vehicle = Vehicle.builder()
                .id(1L)
                .brand("Renault")
                .model("Clio")
                .anneeFabrication(2023)
                .typeCarburant("Essence")
                .garage(garage)
                .build();

        vehicleDTO = VehicleDTO.builder()
                .id(1L)
                .brand("Renault")
                .model("Clio")
                .anneeFabrication(2023)
                .typeCarburant("Essence")
                .garageId(1L)
                .build();
    }

    @Test
    void create_shouldReturnCreatedVehicle() {
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(vehicleRepository.countByGarageId(1L)).thenReturn(0);
        when(vehicleMapper.toEntity(any(VehicleDTO.class))).thenReturn(vehicle);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);
        when(vehicleMapper.toDTO(any(Vehicle.class))).thenReturn(vehicleDTO);

        VehicleDTO result = vehicleService.create(1L, vehicleDTO);

        assertThat(result.getBrand()).isEqualTo("Renault");
        verify(vehicleProducer).sendVehicleCreatedEvent(any(VehicleDTO.class));
    }

    @Test
    void create_shouldThrowException_whenGarageIsFull() {
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(vehicleRepository.countByGarageId(1L)).thenReturn(50);

        assertThatThrownBy(() -> vehicleService.create(1L, vehicleDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("50");
    }

    @Test
    void create_shouldThrowException_whenGarageNotFound() {
        when(garageRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleService.create(99L, vehicleDTO))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void findByGarageId_shouldReturnVehicles() {
        when(garageRepository.existsById(1L)).thenReturn(true);
        when(vehicleRepository.findByGarageId(1L)).thenReturn(List.of(vehicle));
        when(vehicleMapper.toDTO(vehicle)).thenReturn(vehicleDTO);

        List<VehicleDTO> result = vehicleService.findByGarageId(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void findByModel_shouldReturnVehiclesFromMultipleGarages() {
        when(vehicleRepository.findByModel("Clio")).thenReturn(List.of(vehicle));
        when(vehicleMapper.toDTO(vehicle)).thenReturn(vehicleDTO);

        List<VehicleDTO> result = vehicleService.findByModel("Clio");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getModel()).isEqualTo("Clio");
    }

    @Test
    void delete_shouldDeleteVehicle_whenExists() {
        when(vehicleRepository.existsById(1L)).thenReturn(true);

        vehicleService.delete(1L);

        verify(vehicleRepository).deleteById(1L);
    }
}

