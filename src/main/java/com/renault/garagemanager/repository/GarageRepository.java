package com.renault.garagemanager.repository;

import com.renault.garagemanager.entity.GarageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * JPA repository for CRUD operations and custom queries on garages.
 */
public interface GarageRepository extends JpaRepository<GarageEntity, Long> {

    /**
     * Returns all distinct garages that contain at least one vehicle
     * matching the given fuel type.
     *
     * @param fuelType the fuel type to filter by (e.g. "Diesel", "Electric")
     * @return list of matching garages
     */
    @Query("SELECT DISTINCT g FROM GarageEntity g JOIN g.vehicles v WHERE v.fuelType = :fuelType")
    List<GarageEntity> findByVehicleType(@Param("fuelType") String fuelType);

    /**
     * Returns all distinct garages that contain at least one vehicle
     * equipped with an accessory matching the given name.
     *
     * @param accessoryName the accessory name to filter by (e.g. "GPS")
     * @return list of matching garages
     */
    @Query("SELECT DISTINCT g FROM GarageEntity g JOIN g.vehicles v JOIN v.accessories a WHERE a.name = :accessoryName")
    List<GarageEntity> findByAccessoryName(@Param("accessoryName") String accessoryName);
}
