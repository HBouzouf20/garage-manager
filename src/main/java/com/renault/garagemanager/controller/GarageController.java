package com.renault.garagemanager.controller;

import com.renault.garagemanager.dto.GarageDto;
import com.renault.garagemanager.service.GarageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for garage management.
 */
@RestController
@RequestMapping("/api/garages")
@RequiredArgsConstructor
public class GarageController {

    private final GarageService garageService;

    @PostMapping
    public ResponseEntity<GarageDto> createGarage(@Valid @RequestBody GarageDto garageDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(garageService.createGarage(garageDto));
    }

    @GetMapping
    public ResponseEntity<Page<GarageDto>> findAllGarages(@PageableDefault(size = 10, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(garageService.findAllGarages(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GarageDto> findGarageById(@PathVariable Long id) {
        return ResponseEntity.ok(garageService.findGarageById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GarageDto> updateGarage(@PathVariable Long id, @Valid @RequestBody GarageDto garageDto) {
        return ResponseEntity.ok(garageService.updateGarage(id, garageDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGarage(@PathVariable Long id) {
        garageService.deleteGarage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/by-vehicle-type")
    public ResponseEntity<List<GarageDto>> findGaragesByVehicleType(@RequestParam String fuelType) {
        return ResponseEntity.ok(garageService.findGaragesByVehicleType(fuelType));
    }

    @GetMapping("/search/by-accessory")
    public ResponseEntity<List<GarageDto>> findGaragesByAccessoryName(@RequestParam String accessoryName) {
        return ResponseEntity.ok(garageService.findGaragesByAccessoryName(accessoryName));
    }
}

