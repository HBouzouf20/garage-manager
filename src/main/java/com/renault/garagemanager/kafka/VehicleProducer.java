package com.renault.garagemanager.kafka;
import com.renault.garagemanager.dto.VehicleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
/**
 * Kafka producer that publishes an event on every vehicle creation.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class VehicleProducer {

    private static final String TOPIC = "vehicle-created";

    private final KafkaTemplate<String, VehicleDto> kafkaTemplate;

    public void sendVehicleCreatedEvent(VehicleDto vehicleDTO) {
        log.info("Publishing vehicle-created event for vehicle id={}", vehicleDTO.id());
        kafkaTemplate.send(TOPIC, vehicleDTO.id().toString(), vehicleDTO);
    }
}
