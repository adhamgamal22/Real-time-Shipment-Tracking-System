package com.Adham.Shipment.Dto;

import com.Adham.Shipment.Shipment.entites.Shipment;
import com.Adham.Shipment.Shipment.entites.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStatusRequest {

    private ShipmentStatus status;
    private String CurrentLocation;

}