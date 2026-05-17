package com.Adham.Shipment.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateShipmentRequest {
    private BigDecimal price;
    @NotBlank(message = "origin is required")
    private String origin;

    @NotBlank(message = "destination is required")
    private String destination;

    @NotBlank(message = "estimatedDelvirey is required")
    private String estimatedDelivrey;

}
