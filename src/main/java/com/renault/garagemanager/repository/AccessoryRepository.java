package com.renault.garagemanager.repository;

import com.renault.garagemanager.entity.AccessoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * JPA repository for CRUD operations on accessories.
 */
public interface AccessoryRepository extends JpaRepository<AccessoryEntity, Long> {

    /**
     * Returns all accessories attached to the given vehicle.
     *
     * @param vehicleId the vehicle id
     * @return list of accessories for that vehicle
     */
    List<AccessoryEntity> findByVehicleId(Long vehicleId);
}
