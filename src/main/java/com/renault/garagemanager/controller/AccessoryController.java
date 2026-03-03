package com.renault.garagemanager.controller;
import com.renault.garagemanager.dto.AccessoryDTO;
import com.renault.garagemanager.service.AccessoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * Controleur REST pour la gestion des accessoires.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccessoryController {
    private final AccessoryService accessoryService;
    @PostMapping("/vehicles/{vehicleId}/accessories")
    public ResponseEntity<AccessoryDTO> create(@PathVariable Long vehicleId, @Valid @RequestBody AccessoryDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accessoryService.create(vehicleId, dto));
    }
    @GetMapping("/vehicles/{vehicleId}/accessories")
    public ResponseEntity<List<AccessoryDTO>> findByVehicle(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(accessoryService.findByVehicleId(vehicleId));
    }
    @GetMapping("/accessories/{id}")
    public ResponseEntity<AccessoryDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(accessoryService.findById(id));
    }
    @PutMapping("/accessories/{id}")
    public ResponseEntity<AccessoryDTO> update(@PathVariable Long id, @Valid @RequestBody AccessoryDTO dto) {
        return ResponseEntity.ok(accessoryService.update(id, dto));
    }
    @DeleteMapping("/accessories/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accessoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
