package com.Adham.Shipment.controller;

import com.Adham.Shipment.Dto.LocationUpdate;
import com.Adham.Shipment.Dto.LocationResponse;
import com.Adham.Shipment.Services.LocationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Live Location", description = "Live Location Tracking ")
public class LocationController {

    private final LocationService locationService;

    @MessageMapping("/location.update")
    public void updateLocationViaWebSocket(LocationUpdate request) {
	locationService.updateLocation(request);
    }

    @PostMapping("/location/update")
    public ResponseEntity<LocationResponse> updateLocation(@RequestBody @Valid LocationUpdate request) {
	return ResponseEntity.ok(locationService.updateLocation(request));
    }

    @GetMapping("/location/{shipmentId}")
    public ResponseEntity<LocationResponse> getCurrentLocation(@PathVariable UUID shipmentId) {
	return ResponseEntity.ok(locationService.getCurrentLocation(shipmentId));
    }
}