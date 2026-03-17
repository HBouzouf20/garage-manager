package com.renault.garagemanager.controller;

import com.renault.garagemanager.dto.AccessoryDto;
import com.renault.garagemanager.service.AccessoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for accessory management.
 */
@RestController
@RequestMapping("/api/accessories")
@RequiredArgsConstructor
public class AccessoryController {

    private final AccessoryService accessoryService;

    @PostMapping
    public ResponseEntity<AccessoryDto> createAccessory(
            @RequestParam Long vehicleId,
            @Valid @RequestBody AccessoryDto accessoryDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accessoryService.createAccessory(vehicleId, accessoryDto));
    }

    @GetMapping
    public ResponseEntity<List<AccessoryDto>> findAllAccessories() {
        return ResponseEntity.ok(accessoryService.findAllAccessories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccessoryDto> findAccessoryById(@PathVariable Long id) {
        return ResponseEntity.ok(accessoryService.findAccessoryById(id));
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<AccessoryDto>> findAccessoriesByVehicleId(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(accessoryService.findAccessoriesByVehicleId(vehicleId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccessoryDto> updateAccessory(
            @PathVariable Long id,
            @Valid @RequestBody AccessoryDto accessoryDto) {
        return ResponseEntity.ok(accessoryService.updateAccessory(id, accessoryDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccessory(@PathVariable Long id) {
        accessoryService.deleteAccessory(id);
        return ResponseEntity.noContent().build();
    }
}

