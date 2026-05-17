package com.Adham.Shipment.Dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(

	@NotNull Long shipmentid, @NotNull BigDecimal amount, String trackingnumber

) {

}
