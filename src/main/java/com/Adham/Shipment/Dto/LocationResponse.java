package com.Adham.Shipment.Dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class LocationResponse {
    private UUID shipmentId;
    private String trackingNumber;
    private Double latitude;
    private Double longitude;
    private String locationName;
    private LocalDateTime updatedAt;
}