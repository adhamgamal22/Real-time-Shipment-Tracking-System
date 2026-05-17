package com.Adham.Shipment.controller;

import com.Adham.Shipment.Dto.StripePaymentRequest;
import com.Adham.Shipment.Dto.StripePaymentResponse;
import com.Adham.Shipment.Services.StripeService;
import com.Adham.Shipment.config.StripeConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stripe")
@Tag(name = "Stripe", description = "Stripe Payment ")
public class StripeController {

    private final StripeService stripeService;
    private final StripeConfig stripeConfig;

    @PostMapping("/create")
    public ResponseEntity<StripePaymentResponse> create(@RequestBody @Valid StripePaymentRequest request) {
	return ResponseEntity.ok(stripeService.createPaymentIntent(request));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody String payload,
	    @RequestHeader("Stripe-Signature") String sigHeader) {
	stripeService.handleWebhook(payload, sigHeader, stripeConfig.getWebhookSecret());
	return ResponseEntity.ok().build();
    }
}