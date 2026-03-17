package com.renault.garagemanager.controller;

import com.renault.garagemanager.dto.VehicleDto;
import com.renault.garagemanager.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for vehicle management.
 */
@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleDto> createVehicle(
            @RequestParam Long garageId,
            @Valid @RequestBody VehicleDto vehicleDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vehicleService.createVehicle(garageId, vehicleDto));
    }

    @GetMapping
    public ResponseEntity<List<VehicleDto>> findAllVehicles() {
        return ResponseEntity.ok(vehicleService.findAllVehicles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDto> findVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.findVehicleById(id));
    }

    @GetMapping("/garage/{garageId}")
    public ResponseEntity<List<VehicleDto>> findVehiclesByGarageId(@PathVariable Long garageId) {
        return ResponseEntity.ok(vehicleService.findVehiclesByGarageId(garageId));
    }

    @GetMapping("/search/by-model")
    public ResponseEntity<List<VehicleDto>> findVehiclesByModel(@RequestParam String model) {
        return ResponseEntity.ok(vehicleService.findVehiclesByModel(model));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDto> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody VehicleDto vehicleDto) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, vehicleDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}

