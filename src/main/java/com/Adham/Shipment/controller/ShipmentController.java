package com.Adham.Shipment.controller;

import com.Adham.Shipment.Dto.CreateShipmentRequest;
import com.Adham.Shipment.Dto.ShipmentResponse;
import com.Adham.Shipment.Dto.UpdateStatusRequest;
import com.Adham.Shipment.Services.ShipmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Shipment", description = "Shipment Management APIs")
@RestController
@RequestMapping("/api/shipment")
public class ShipmentController {
    @Autowired
    private ShipmentService shipmentServices;

    @GetMapping("/getall")
    public ResponseEntity<?> getallshipments() {

	var shipments = shipmentServices.getAllShipments();
	return ResponseEntity.status(HttpStatus.OK).body(shipments);
    }

    @PreAuthorize("@Security.isOwner(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<?> getshipmentbyID(@PathVariable UUID id) {

	var shipment = shipmentServices.getShipmentbyID(id);
	return ResponseEntity.status(HttpStatus.OK).body(shipment);
    }

    @PreAuthorize("@Security.isOwner(#id)")

    @GetMapping("/track/{trackingnumber}")
    public ResponseEntity<?> getshipmentbytrackingnumber(@PathVariable String trackingnumber) {

	var shipment = shipmentServices.getShipmentbytrackingnumber(trackingnumber);
	return ResponseEntity.status(HttpStatus.OK).body(shipment);
    }

    @PostMapping("/create")
    public ResponseEntity<ShipmentResponse> createshipment(@RequestBody CreateShipmentRequest request) {
	var shipment = shipmentServices.createshipment(request);
	return ResponseEntity.status(HttpStatus.CREATED).body(shipment);
    }

    @PreAuthorize("@Security.isOwner(#id)")

    @PutMapping("/{id}/status")
    public ResponseEntity<ShipmentResponse> uptdateshipmentstatus(@PathVariable UUID id,
	    @Valid @RequestBody UpdateStatusRequest request) {

	var shipment = shipmentServices.updateStatusRequest(id, request);
	return ResponseEntity.status(HttpStatus.OK).body(shipment);
    }

}
