package com.renault.garagemanager.repository;

import com.renault.garagemanager.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * JPA repository for CRUD operations on vehicles.
 */
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {

    /**
     * Returns all vehicles belonging to the given garage.
     *
     * @param garageId the garage id
     * @return list of vehicles in that garage
     */
    List<VehicleEntity> findByGarageId(Long garageId);

    /**
     * Returns all vehicles matching the given model name.
     *
     * @param model the vehicle model (e.g. "Clio")
     * @return list of matching vehicles
     */
    List<VehicleEntity> findByModel(String model);

    /**
     * Returns the total number of vehicles stored in the given garage.
     *
     * @param garageId the garage id
     * @return vehicle count
     */
    int countByGarageId(Long garageId);
}
