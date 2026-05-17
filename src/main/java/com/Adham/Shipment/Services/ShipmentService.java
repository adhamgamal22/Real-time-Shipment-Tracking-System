package com.Adham.Shipment.Services;

import com.Adham.Shipment.Dto.CreateShipmentRequest;
import com.Adham.Shipment.Dto.ShipmentResponse;
import com.Adham.Shipment.Dto.StatusUpdateMessage;
import com.Adham.Shipment.Dto.UpdateStatusRequest;
import com.Adham.Shipment.Repository.ShipmentRepository;
import com.Adham.Shipment.Shared.CustomResponseException;
import com.Adham.Shipment.Shipment.entites.Shipment;
import com.Adham.Shipment.Shipment.entites.ShipmentStatus;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ShipmentService {

    private ShipmentRepository shipmentRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ShipmentResponse createshipment(CreateShipmentRequest request) {
	String trackingnumber = generateTrackingNumber();
	Shipment shipment = Shipment.builder().trackingNumber(trackingnumber).origin(request.getOrigin())
		.destination(request.getDestination())
		.estimatedDeliveryDate(LocalDate.parse(request.getEstimatedDelivrey())).price(request.getPrice())
		.build();
	shipmentRepository.save(shipment);
	notifyShipmentStatus(shipment, getStatusMessage(shipment.getStatus()));

	return mapToResponse(shipment);
    }

    public List<ShipmentResponse> getAllShipments() {
	List<Shipment> shipments = shipmentRepository.findAll();
	return shipments.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public ShipmentResponse getShipmentbyID(UUID id) {
	Shipment shipment = shipmentRepository.findById(id).orElseThrow(
		() -> new CustomResponseException("SHIPMENT_NOT_FOUND", "Shipment with id " + id + " not found"));

	return mapToResponse(shipment);

    }

    public ShipmentResponse getShipmentbytrackingnumber(String trackingnumber) {
	Shipment shipment = shipmentRepository.findByTrackingNumber(trackingnumber)
		.orElseThrow(() -> new RuntimeException("this tracking-number not exist: " + trackingnumber));
	return mapToResponse(shipment);
    }

    public ShipmentResponse updateStatusRequest(UUID id, UpdateStatusRequest request) {
	Shipment shipment = shipmentRepository.findById(id)
		.orElseThrow(() -> new RuntimeException("this ID not found: " + id));
	shipment.setStatus(request.getStatus());
	if (request.getCurrentLocation() != null) {
	    shipment.setCurrentLocation(request.getCurrentLocation());
	}
	shipmentRepository.save(shipment);
	notifyShipmentStatus(shipment, getStatusMessage(shipment.getStatus()));

	return mapToResponse(shipment);
    }

    private String generateTrackingNumber() {
	return "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private ShipmentResponse mapToResponse(Shipment shipment) {
	return ShipmentResponse.builder().shipmentId(shipment.getShipmentId())
		.tracking_num(shipment.getTrackingNumber()).origin(shipment.getOrigin())
		.destination(shipment.getDestination()).status(shipment.getStatus()).createdAt(shipment.getCreatedAt())
		.updatedAt(shipment.getUpdatedAt()).CurrentLocation(shipment.getCurrentLocation())
		.EstimatedLocation(String.valueOf(shipment.getEstimatedDeliveryDate())).build();

    }

    public void notifyShipmentStatus(Shipment shipment, String message) {
	var update = StatusUpdateMessage.builder().trackingNumber(shipment.getTrackingNumber())
		.status(shipment.getStatus()).currentLocation(shipment.getCurrentLocation())
		.timestamp(shipment.getUpdatedAt()).message(message).build();

//	messagingTemplate.convertAndSend("/topic/shipments", update);
	messagingTemplate.convertAndSend("/topic/shipments/" + shipment.getShipmentId(), update);

	if (shipment == null || shipment.getShipmentId() == null) {
	    log.warn("Cannot send notification: shipment or shipmentId is null");
	    return;
	}
    }

    private String getStatusMessage(ShipmentStatus status) {
	return switch (status) {
	case ORDER_PLACED -> "Order has been placed";
	case PROCESSING -> "Order is being processed";
	case PICKED_UP -> "Package has been picked up";
	case IN_TRANSIT -> "Package is in transit";
	case OUT_FOR_DELIVERY -> "Package is out for delivery";
	case DELIVERED -> "Package has been delivered";
	case EXCEPTION -> "Delivery exception occurred";
	default -> throw new IllegalStateException("Unexpected value: " + status);
	};
    }
}
