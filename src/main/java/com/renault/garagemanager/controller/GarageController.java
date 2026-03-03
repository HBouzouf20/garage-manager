package com.renault.garagemanager.controller;
import com.renault.garagemanager.dto.GarageDTO;
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
 * Controleur REST pour la gestion des garages.
 */
@RestController
@RequestMapping("/api/garages")
@RequiredArgsConstructor
public class GarageController {
    private final GarageService garageService;
    @PostMapping
    public ResponseEntity<GarageDTO> create(@Valid @RequestBody GarageDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(garageService.create(dto));
    }
    @GetMapping("/{id}")
    public ResponseEntity<GarageDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(garageService.findById(id));
    }
    @GetMapping
    public ResponseEntity<Page<GarageDTO>> findAll(@PageableDefault(size = 10, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(garageService.findAll(pageable));
    }
    @PutMapping("/{id}")
    public ResponseEntity<GarageDTO> update(@PathVariable Long id, @Valid @RequestBody GarageDTO dto) {
        return ResponseEntity.ok(garageService.update(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        garageService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/search/by-vehicle-type")
    public ResponseEntity<List<GarageDTO>> searchByVehicleType(@RequestParam String typeCarburant) {
        return ResponseEntity.ok(garageService.findByVehicleType(typeCarburant));
    }
    @GetMapping("/search/by-accessory")
    public ResponseEntity<List<GarageDTO>> searchByAccessory(@RequestParam String accessoryName) {
        return ResponseEntity.ok(garageService.findByAccessoryName(accessoryName));
    }
}
