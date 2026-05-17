package com.Adham.Shipment.Services;

import com.Adham.Shipment.Dto.LocationUpdate;
import com.Adham.Shipment.Dto.LocationResponse;
import com.Adham.Shipment.Repository.ShipmentRepository;
import com.Adham.Shipment.Shared.CustomResponseException;
import com.Adham.Shipment.Shipment.entites.Shipment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService {

    private final ShipmentRepository shipmentRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    public LocationResponse updateLocation(LocationUpdate request) {
	Shipment shipment = shipmentRepository.findById(request.getShipmentId())
		.orElseThrow(() -> new CustomResponseException("SHIPMENT_NOT_FOUND", "Shipment not found"));

	shipment.setCurrentLatitude(request.getLatitude());
	shipment.setCurrentLongitude(request.getLongitude());
	shipment.setCurrentLocation(request.getLocationName());
	shipment.setUpdatedAt(LocalDateTime.now());
	shipmentRepository.save(shipment);

	LocationResponse response = LocationResponse.builder().shipmentId(shipment.getShipmentId())
		.trackingNumber(shipment.getTrackingNumber()).latitude(request.getLatitude())
		.longitude(request.getLongitude()).locationName(request.getLocationName())
		.updatedAt(LocalDateTime.now()).build();

	messagingTemplate.convertAndSendToUser(shipment.getUser().getEmail(), "/queue/location", response);

	messagingTemplate.convertAndSend("/topic/shipment/" + shipment.getTrackingNumber() + "/location", response);

	log.info("Location updated for shipment: {}", shipment.getTrackingNumber());

	return response;
    }

    public LocationResponse getCurrentLocation(UUID shipmentId) {
	Shipment shipment = shipmentRepository.findById(shipmentId)
		.orElseThrow(() -> new CustomResponseException("SHIPMENT_NOT_FOUND", "Shipment not found"));

	return LocationResponse.builder().shipmentId(shipment.getShipmentId())
		.trackingNumber(shipment.getTrackingNumber()).latitude(shipment.getCurrentLatitude())
		.longitude(shipment.getCurrentLongitude()).locationName(shipment.getCurrentLocation())
		.updatedAt(shipment.getUpdatedAt()).build();
    }
}