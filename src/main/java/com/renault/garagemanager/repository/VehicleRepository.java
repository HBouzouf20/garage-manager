package com.renault.garagemanager.repository;
import com.renault.garagemanager.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
/**
 * Repository JPA pour les operations CRUD sur les vehicules.
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByGarageId(Long garageId);
    List<Vehicle> findByModel(String model);
    int countByGarageId(Long garageId);
}
