package com.Adham.Shipment.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class StripePaymentRequest {
    @NotBlank
    private String trackingNumber;
    @NotNull
    private BigDecimal amount;
    @NotBlank
    private String currency; // "usd" or "egp"
}