package com.renault.garagemanager.kafka;

import com.renault.garagemanager.dto.VehicleDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer that processes vehicle creation events.
 */
@Component
@Slf4j
public class VehicleConsumer {

    @KafkaListener(topics = "vehicle-created", groupId = "garage-manager-group")
    public void onVehicleCreated(VehicleDto vehicleDTO) {
        log.info("Event received: vehicle created — id={}, brand={}, model={}",
                vehicleDTO.id(), vehicleDTO.brand(), vehicleDTO.model());
    }
}
