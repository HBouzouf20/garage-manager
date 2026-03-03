package com.renault.garagemanager.service;
import com.renault.garagemanager.dto.GarageDTO;
import com.renault.garagemanager.entity.Garage;
import com.renault.garagemanager.exception.ResourceNotFoundException;
import com.renault.garagemanager.mapper.GarageMapper;
import com.renault.garagemanager.repository.GarageRepository;
import com.renault.garagemanager.service.impl.GarageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
/**
 * Tests unitaires du service GarageService.
 */
@ExtendWith(MockitoExtension.class)
class GarageServiceTest {
    @Mock
    private GarageRepository garageRepository;
    @Mock
    private GarageMapper garageMapper;
    @InjectMocks
    private GarageServiceImpl garageService;
    private Garage garage;
    private GarageDTO garageDTO;
    @BeforeEach
    void setUp() {
        garage = Garage.builder()
                .id(1L)
                .name("Garage Renault Paris")
                .address("10 rue de la Paix, Paris")
                .telephone("0145678900")
                .email("paris@renault.fr")
                .build();
        garageDTO = GarageDTO.builder()
                .id(1L)
                .name("Garage Renault Paris")
                .address("10 rue de la Paix, Paris")
                .telephone("0145678900")
                .email("paris@renault.fr")
                .build();
    }
    @Test
    void create_shouldReturnCreatedGarage() {
        when(garageMapper.toEntity(any(GarageDTO.class))).thenReturn(garage);
        when(garageRepository.save(any(Garage.class))).thenReturn(garage);
        when(garageMapper.toDTO(any(Garage.class))).thenReturn(garageDTO);
        GarageDTO result = garageService.create(garageDTO);
        assertThat(result.getName()).isEqualTo("Garage Renault Paris");
        verify(garageRepository).save(any(Garage.class));
    }
    @Test
    void findById_shouldReturnGarage_whenExists() {
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(garageMapper.toDTO(garage)).thenReturn(garageDTO);
        GarageDTO result = garageService.findById(1L);
        assertThat(result.getId()).isEqualTo(1L);
    }
    @Test
    void findById_shouldThrowException_whenNotExists() {
        when(garageRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> garageService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
    @Test
    void findAll_shouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Garage> page = new PageImpl<>(List.of(garage));
        when(garageRepository.findAll(pageable)).thenReturn(page);
        when(garageMapper.toDTO(garage)).thenReturn(garageDTO);
        Page<GarageDTO> result = garageService.findAll(pageable);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }
    @Test
    void update_shouldReturnUpdatedGarage() {
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(garageRepository.save(any(Garage.class))).thenReturn(garage);
        when(garageMapper.toDTO(any(Garage.class))).thenReturn(garageDTO);
        GarageDTO result = garageService.update(1L, garageDTO);
        assertThat(result).isNotNull();
        verify(garageMapper).updateEntity(garage, garageDTO);
    }
    @Test
    void delete_shouldDeleteGarage_whenExists() {
        when(garageRepository.existsById(1L)).thenReturn(true);
        garageService.delete(1L);
        verify(garageRepository).deleteById(1L);
    }
    @Test
    void delete_shouldThrowException_whenNotExists() {
        when(garageRepository.existsById(99L)).thenReturn(false);
        assertThatThrownBy(() -> garageService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}