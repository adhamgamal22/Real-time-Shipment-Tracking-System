package com.Adham.Shipment.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StripePaymentResponse {
    private String clientSecret;
    private String paymentIntentId;
    private String status;
    private String trackingNumber;
}