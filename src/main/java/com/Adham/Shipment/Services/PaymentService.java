package com.Adham.Shipment.Services;

import com.Adham.Shipment.Dto.PaymentRequest;
import com.Adham.Shipment.Repository.PaymentRepo;
import com.Adham.Shipment.Repository.ShipmentRepository;
import com.Adham.Shipment.Shared.CustomResponseException;
import com.Adham.Shipment.Shipment.entites.Payment;
import com.Adham.Shipment.Shipment.entites.PaymentStatus;
import com.Adham.Shipment.Shipment.entites.Shipment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepo paymentRepo;
    private final ShipmentRepository shipmentRepository;
    private final NotificationService notificationService;

    @Transactional
    public Payment createPayment(PaymentRequest request) {
	validatePaymentRequest(request);
	log.info("Creating payment for tracking number: {}", request.trackingnumber());

	Shipment shipment = shipmentRepository.findByTrackingNumber(request.trackingnumber())
		.orElseThrow(() -> new CustomResponseException("SHIPMENT_NOT_FOUND",
			"Shipment with tracking number " + request.trackingnumber() + " not found"));

	Optional<Payment> existingPayment = paymentRepo.findByShipment(shipment);

	if (existingPayment.isPresent()) {
	    Payment payment = existingPayment.get();

	    if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
		throw new CustomResponseException("ALREADY_PAID",
			"Shipment with tracking number " + request.trackingnumber() + " is already paid");
	    }

	    payment.setAmount(request.amount());
	    payment.setPaymentStatus(PaymentStatus.PENDING);
	    payment.setUpdatedAt(LocalDateTime.now());

	    Payment updatedPayment = paymentRepo.save(payment);

	    notificationService.sendNotification(shipment.getUser(),
		    "THE SHIPMENT IS UPDATE " + shipment.getTrackingNumber());

	    return updatedPayment;
	}

	return createNewPayment(shipment, request);
    }

    @Transactional
    public Payment processPayment(String trackingNumber) {
	log.info("Processing payment for tracking number: {}", trackingNumber);

	Shipment shipment = shipmentRepository.findByTrackingNumber(trackingNumber)
		.orElseThrow(() -> new CustomResponseException("SHIPMENT_NOT_FOUND",
			"Shipment with tracking number " + trackingNumber + " not found"));

	Payment payment = paymentRepo.findByShipment(shipment)
		.orElseThrow(() -> new CustomResponseException("PAYMENT_NOT_FOUND",
			"No payment found for tracking number " + trackingNumber));

	if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
	    throw new CustomResponseException("ALREADY_PAID",
		    "Shipment with tracking number " + trackingNumber + " is already paid");
	}

	if (payment.getPaymentStatus() == PaymentStatus.FAILED) {
	    throw new CustomResponseException("PAYMENT_FAILED",
		    "Payment for tracking number " + trackingNumber + " has failed");
	}

	payment.setPaymentStatus(PaymentStatus.SUCCESS);
	payment.setUpdatedAt(LocalDateTime.now());

	Payment processedPayment = paymentRepo.save(payment);

	notificationService.sendNotification(shipment.getUser(), "THE PAYMENT SUCCESSED " + trackingNumber);

	return processedPayment;
    }

    @Transactional
    public Payment canclePayment(String trackingNumber) {
	log.info("Cancelling payment for tracking number: {}", trackingNumber);

	Shipment shipment = shipmentRepository.findByTrackingNumber(trackingNumber)
		.orElseThrow(() -> new CustomResponseException("SHIPMENT_NOT_FOUND",
			"Shipment with tracking number " + trackingNumber + " not found"));

	Payment payment = paymentRepo.findByShipment(shipment)
		.orElseThrow(() -> new CustomResponseException("PAYMENT_NOT_FOUND",
			"No payment found for tracking number " + trackingNumber));

	if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
	    throw new CustomResponseException("CANNOT_CANCEL", "Cannot cancel a successfully paid shipment");
	}

	if (payment.getPaymentStatus() == PaymentStatus.FAILED) {
	    throw new CustomResponseException("ALREADY_FAILED", "Payment is already failed/cancelled");
	}

	payment.setPaymentStatus(PaymentStatus.FAILED);
	payment.setUpdatedAt(LocalDateTime.now());

	Payment cancelledPayment = paymentRepo.save(payment);

	notificationService.sendNotification(shipment.getUser(), "THE SHIPMENT IS CANCLED " + trackingNumber);

	return cancelledPayment;
    }

    public Payment createNewPayment(Shipment shipment, PaymentRequest request) {
	log.info("Creating new payment for shipment: {}", shipment.getTrackingNumber());

	Payment newPayment = Payment.builder().amount(request.amount()).paymentStatus(PaymentStatus.PENDING)
		.shipment(shipment).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

	Payment savedPayment = paymentRepo.save(newPayment);

	return savedPayment;
    }

    public List<Payment> getall() {
	return paymentRepo.findAll();
    }

    @Transactional
    public Payment createPaymentByShipmentId(UUID shipmentId, BigDecimal amount) {
	Shipment shipment = shipmentRepository.findById(shipmentId)
		.orElseThrow(() -> new CustomResponseException("SHIPMENT_NOT_FOUND",
			"Shipment with ID " + shipmentId + " not found"));

	Optional<Payment> existingPayment = paymentRepo.findByShipment(shipment);

	if (existingPayment.isPresent()) {
	    Payment payment = existingPayment.get();
	    if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
		throw new CustomResponseException("ALREADY_PAID", "Shipment is already paid");
	    }
	    payment.setAmount(amount);
	    payment.setPaymentStatus(PaymentStatus.PENDING);
	    payment.setUpdatedAt(LocalDateTime.now());
	    return paymentRepo.save(payment);
	}

	Payment newPayment = Payment.builder().amount(amount).paymentStatus(PaymentStatus.PENDING).shipment(shipment)
		.method("MANUAL").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

	return paymentRepo.save(newPayment);
    }

    private void validatePaymentRequest(PaymentRequest request) {
	if (request.trackingnumber() == null || request.trackingnumber().isBlank()) {
	    throw new CustomResponseException("INVALID_TRACKING_NUMBER", "Tracking number must not be empty");
	}
	if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
	    throw new CustomResponseException("INVALID_AMOUNT", "Amount must be greater than zero");
	}
    }
}