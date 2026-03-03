package com.renault.garagemanager.controller;
import com.renault.garagemanager.dto.VehicleDTO;
import com.renault.garagemanager.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * Controleur REST pour la gestion des vehicules.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;
    @PostMapping("/garages/{garageId}/vehicles")
    public ResponseEntity<VehicleDTO> create(@PathVariable Long garageId, @Valid @RequestBody VehicleDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.create(garageId, dto));
    }
    @GetMapping("/garages/{garageId}/vehicles")
    public ResponseEntity<List<VehicleDTO>> findByGarage(@PathVariable Long garageId) {
        return ResponseEntity.ok(vehicleService.findByGarageId(garageId));
    }
    @GetMapping("/vehicles/{id}")
    public ResponseEntity<VehicleDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.findById(id));
    }
    @GetMapping("/vehicles/search/by-model")
    public ResponseEntity<List<VehicleDTO>> findByModel(@RequestParam String model) {
        return ResponseEntity.ok(vehicleService.findByModel(model));
    }
    @PutMapping("/vehicles/{id}")
    public ResponseEntity<VehicleDTO> update(@PathVariable Long id, @Valid @RequestBody VehicleDTO dto) {
        return ResponseEntity.ok(vehicleService.update(id, dto));
    }
    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
