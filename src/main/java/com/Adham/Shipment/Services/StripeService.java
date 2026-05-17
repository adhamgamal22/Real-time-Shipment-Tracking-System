package com.Adham.Shipment.Services;

import com.Adham.Shipment.Dto.StripePaymentRequest;
import com.Adham.Shipment.Dto.StripePaymentResponse;
import com.Adham.Shipment.Repository.PaymentRepo;
import com.Adham.Shipment.Repository.ShipmentRepository;
import com.Adham.Shipment.Shared.CustomResponseException;
import com.Adham.Shipment.Shipment.entites.Payment;
import com.Adham.Shipment.Shipment.entites.PaymentStatus;
import com.Adham.Shipment.Shipment.entites.Shipment;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripeService {

    private final ShipmentRepository shipmentRepository;
    private final PaymentRepo paymentRepo;
    private final NotificationService notificationService;

    public StripePaymentResponse createPaymentIntent(StripePaymentRequest request) {
	Shipment shipment = shipmentRepository.findByTrackingNumber(request.getTrackingNumber())
		.orElseThrow(() -> new CustomResponseException("SHIPMENT_NOT_FOUND",
			"Shipment not found: " + request.getTrackingNumber()));

	try {
	    PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
		    .setAmount(request.getAmount().multiply(new java.math.BigDecimal("100")).longValue())
		    .setCurrency(request.getCurrency()).putMetadata("trackingNumber", request.getTrackingNumber())
		    .build();

	    PaymentIntent intent = PaymentIntent.create(params);

	    log.info("PaymentIntent created: {}", intent.getId());

	    return StripePaymentResponse.builder().clientSecret(intent.getClientSecret())
		    .paymentIntentId(intent.getId()).status(intent.getStatus())
		    .trackingNumber(request.getTrackingNumber()).build();

	} catch (StripeException e) {
	    log.error("Stripe error: {}", e.getMessage());
	    throw new CustomResponseException("STRIPE_ERROR", e.getMessage());
	}
    }

    public void handleWebhook(String payload, String sigHeader, String webhookSecret) {
	com.stripe.model.Event event;

	try {
	    event = com.stripe.net.Webhook.constructEvent(payload, sigHeader, webhookSecret);
	} catch (Exception e) {
	    throw new CustomResponseException("INVALID_WEBHOOK", "Invalid webhook signature");
	}

	if ("payment_intent.succeeded".equals(event.getType())) {
	    PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElseThrow();

	    String trackingNumber = intent.getMetadata().get("trackingNumber");

	    Shipment shipment = shipmentRepository.findByTrackingNumber(trackingNumber)
		    .orElseThrow(() -> new CustomResponseException("SHIPMENT_NOT_FOUND", "Shipment not found"));

	    Payment payment = paymentRepo.findByShipment(shipment)
		    .orElseThrow(() -> new CustomResponseException("PAYMENT_NOT_FOUND", "Payment not found"));

	    payment.setPaymentStatus(PaymentStatus.SUCCESS);
	    payment.setUpdatedAt(LocalDateTime.now());
	    paymentRepo.save(payment);

	    notificationService.sendNotification(shipment.getUser(),
		    "Payment successfully processed via Stripe - Tracking number" + trackingNumber);

	    log.info("Payment succeeded for tracking: {}", trackingNumber);
	}

	if ("payment_intent.payment_failed".equals(event.getType())) {
	    PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElseThrow();

	    String trackingNumber = intent.getMetadata().get("trackingNumber");

	    Shipment shipment = shipmentRepository.findByTrackingNumber(trackingNumber)
		    .orElseThrow(() -> new CustomResponseException("SHIPMENT_NOT_FOUND", "Shipment not found"));

	    Payment payment = paymentRepo.findByShipment(shipment)
		    .orElseThrow(() -> new CustomResponseException("PAYMENT_NOT_FOUND", "Payment not found"));

	    payment.setPaymentStatus(PaymentStatus.FAILED);
	    payment.setUpdatedAt(LocalDateTime.now());
	    paymentRepo.save(payment);

	    notificationService.sendNotification(shipment.getUser(), "PAYMENT FAILED " + trackingNumber);

	    log.info("Payment failed for tracking: {}", trackingNumber);
	}
    }
}