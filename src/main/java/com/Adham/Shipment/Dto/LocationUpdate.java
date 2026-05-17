package com.Adham.Shipment.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class LocationUpdate {
    @NotNull
    private UUID shipmentId;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    private String locationName;
}