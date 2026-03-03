package com.renault.garagemanager.mapper;
import com.renault.garagemanager.dto.VehicleDTO;
import com.renault.garagemanager.entity.Vehicle;
import org.springframework.stereotype.Component;
/**
 * Mapper bidirectionnel entre l'entite Vehicle et son DTO.
 */
@Component
public class VehicleMapper {
    public VehicleDTO toDTO(Vehicle vehicle) {
        return VehicleDTO.builder()
                .id(vehicle.getId())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .anneeFabrication(vehicle.getAnneeFabrication())
                .typeCarburant(vehicle.getTypeCarburant())
                .garageId(vehicle.getGarage().getId())
                .build();
    }
    public Vehicle toEntity(VehicleDTO dto) {
        return Vehicle.builder()
                .id(dto.getId())
                .brand(dto.getBrand())
                .model(dto.getModel())
                .anneeFabrication(dto.getAnneeFabrication())
                .typeCarburant(dto.getTypeCarburant())
                .build();
    }
    public void updateEntity(Vehicle vehicle, VehicleDTO dto) {
        vehicle.setBrand(dto.getBrand());
        vehicle.setModel(dto.getModel());
        vehicle.setAnneeFabrication(dto.getAnneeFabrication());
        vehicle.setTypeCarburant(dto.getTypeCarburant());
    }
}
