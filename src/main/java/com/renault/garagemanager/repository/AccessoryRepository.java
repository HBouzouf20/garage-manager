package com.renault.garagemanager.repository;
import com.renault.garagemanager.entity.Accessory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
/**
 * Repository JPA pour les operations CRUD sur les accessoires.
 */
public interface AccessoryRepository extends JpaRepository<Accessory, Long> {
    List<Accessory> findByVehicleId(Long vehicleId);
}
