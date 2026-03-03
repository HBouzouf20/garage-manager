package com.renault.garagemanager.mapper;
import com.renault.garagemanager.dto.AccessoryDTO;
import com.renault.garagemanager.entity.Accessory;
import org.springframework.stereotype.Component;
/**
 * Mapper bidirectionnel entre l'entite Accessory et son DTO.
 */
@Component
public class AccessoryMapper {
    public AccessoryDTO toDTO(Accessory accessory) {
        return AccessoryDTO.builder()
                .id(accessory.getId())
                .nom(accessory.getNom())
                .description(accessory.getDescription())
                .prix(accessory.getPrix())
                .type(accessory.getType())
                .vehicleId(accessory.getVehicle().getId())
                .build();
    }
    public Accessory toEntity(AccessoryDTO dto) {
        return Accessory.builder()
                .id(dto.getId())
                .nom(dto.getNom())
                .description(dto.getDescription())
                .prix(dto.getPrix())
                .type(dto.getType())
                .build();
    }
    public void updateEntity(Accessory accessory, AccessoryDTO dto) {
        accessory.setNom(dto.getNom());
        accessory.setDescription(dto.getDescription());
        accessory.setPrix(dto.getPrix());
        accessory.setType(dto.getType());
    }
}
