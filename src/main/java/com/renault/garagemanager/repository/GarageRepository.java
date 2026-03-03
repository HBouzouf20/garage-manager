package com.renault.garagemanager.repository;
import com.renault.garagemanager.entity.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
/**
 * Repository JPA pour les operations CRUD et recherches sur les garages.
 */
public interface GarageRepository extends JpaRepository<Garage, Long> {
    @Query("SELECT DISTINCT g FROM Garage g JOIN g.vehicles v WHERE v.typeCarburant = :typeCarburant")
    List<Garage> findByVehicleType(@Param("typeCarburant") String typeCarburant);
    @Query("SELECT DISTINCT g FROM Garage g JOIN g.vehicles v JOIN v.accessories a WHERE a.nom = :accessoryName")
    List<Garage> findByAccessoryName(@Param("accessoryName") String accessoryName);
}
