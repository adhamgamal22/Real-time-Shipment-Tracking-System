package com.Adham.Shipment.Dto;

import com.Adham.Shipment.Shipment.entites.ShipmentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShipmentResponse {

    private UUID shipmentId;
    private String tracking_num;
    private String origin;
    private String destination;
    private ShipmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String CurrentLocation;
    private String EstimatedLocation;
    private LocalDateTime LocalDateTime;

}
