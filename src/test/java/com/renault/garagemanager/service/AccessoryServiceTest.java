package com.renault.garagemanager.service;

import com.renault.garagemanager.dto.AccessoryDTO;
import com.renault.garagemanager.entity.Accessory;
import com.renault.garagemanager.entity.Vehicle;
import com.renault.garagemanager.exception.ResourceNotFoundException;
import com.renault.garagemanager.mapper.AccessoryMapper;
import com.renault.garagemanager.repository.AccessoryRepository;
import com.renault.garagemanager.repository.VehicleRepository;
import com.renault.garagemanager.service.impl.AccessoryServiceImpl;
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
 * Tests unitaires du service AccessoryService.
 */
@ExtendWith(MockitoExtension.class)
class AccessoryServiceTest {

    @Mock
    private AccessoryRepository accessoryRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private AccessoryMapper accessoryMapper;

    @InjectMocks
    private AccessoryServiceImpl accessoryService;

    private Vehicle vehicle;
    private Accessory accessory;
    private AccessoryDTO accessoryDTO;

    @BeforeEach
    void setUp() {
        vehicle = Vehicle.builder().id(1L).brand("Renault").model("Clio").build();

        accessory = Accessory.builder()
                .id(1L)
                .nom("GPS")
                .description("Systeme de navigation")
                .prix(299.99)
                .type("Electronique")
                .vehicle(vehicle)
                .build();

        accessoryDTO = AccessoryDTO.builder()
                .id(1L)
                .nom("GPS")
                .description("Systeme de navigation")
                .prix(299.99)
                .type("Electronique")
                .vehicleId(1L)
                .build();
    }

    @Test
    void create_shouldReturnCreatedAccessory() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(accessoryMapper.toEntity(any(AccessoryDTO.class))).thenReturn(accessory);
        when(accessoryRepository.save(any(Accessory.class))).thenReturn(accessory);
        when(accessoryMapper.toDTO(any(Accessory.class))).thenReturn(accessoryDTO);

        AccessoryDTO result = accessoryService.create(1L, accessoryDTO);

        assertThat(result.getNom()).isEqualTo("GPS");
    }

    @Test
    void create_shouldThrowException_whenVehicleNotFound() {
        when(vehicleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accessoryService.create(99L, accessoryDTO))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void findByVehicleId_shouldReturnAccessories() {
        when(vehicleRepository.existsById(1L)).thenReturn(true);
        when(accessoryRepository.findByVehicleId(1L)).thenReturn(List.of(accessory));
        when(accessoryMapper.toDTO(accessory)).thenReturn(accessoryDTO);

        List<AccessoryDTO> result = accessoryService.findByVehicleId(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void delete_shouldDeleteAccessory_whenExists() {
        when(accessoryRepository.existsById(1L)).thenReturn(true);

        accessoryService.delete(1L);

        verify(accessoryRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrowException_whenNotExists() {
        when(accessoryRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> accessoryService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

