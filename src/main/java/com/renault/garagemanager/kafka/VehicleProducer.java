package com.renault.garagemanager.kafka;
import com.renault.garagemanager.dto.VehicleDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
/**
 * Producteur Kafka qui publie un evenement a chaque creation de vehicule.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class VehicleProducer {
    private static final String TOPIC = "vehicle-created";
    private final KafkaTemplate<String, VehicleDTO> kafkaTemplate;
    public void sendVehicleCreatedEvent(VehicleDTO vehicleDTO) {
        log.info("Publication evenement vehicle-created pour vehicule id={}", vehicleDTO.getId());
        kafkaTemplate.send(TOPIC, vehicleDTO.getId().toString(), vehicleDTO);
    }
}
