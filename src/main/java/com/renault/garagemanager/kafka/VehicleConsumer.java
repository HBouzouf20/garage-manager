package com.renault.garagemanager.kafka;
import com.renault.garagemanager.dto.VehicleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
/**
 * Consommateur Kafka qui traite les evenements de creation de vehicule.
 */
@Component
@Slf4j
public class VehicleConsumer {
    @KafkaListener(topics = "vehicle-created", groupId = "garage-manager-group")
    public void onVehicleCreated(VehicleDTO vehicleDTO) {
        log.info("Evenement recu : vehicule cree - id={}, brand={}, model={}",
                vehicleDTO.getId(), vehicleDTO.getBrand(), vehicleDTO.getModel());
    }
}
