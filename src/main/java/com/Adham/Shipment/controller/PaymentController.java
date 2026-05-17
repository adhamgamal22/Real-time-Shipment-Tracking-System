package com.Adham.Shipment.controller;

import com.Adham.Shipment.Dto.PaymentRequest;
import com.Adham.Shipment.Services.PaymentService;
import com.Adham.Shipment.Shipment.entites.Payment;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Tag(name = "Payment", description = "Payment Management APIs")

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")

public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/getall")
    public ResponseEntity<List<Payment>> getAll() {
	List<Payment> payments = paymentService.getall();
	return ResponseEntity.status(HttpStatus.OK).body(payments);
    }

    @PostMapping("/process")
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentRequest paymentRequest) {
	Payment payment = paymentService.createPayment(paymentRequest);
	return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @PostMapping("/shipment/{shipmentId}")
    public ResponseEntity<Payment> createPaymentByShipmentId(@PathVariable UUID shipmentId,
	    @RequestParam BigDecimal amount) {
	Payment payment = paymentService.createPaymentByShipmentId(shipmentId, amount);
	return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @PutMapping("/{trackingNumber}/cancle")
    public ResponseEntity<Payment> canclePayment(@PathVariable String trackingnumber) {
	Payment payment = paymentService.canclePayment(trackingnumber);
	return ResponseEntity.status(HttpStatus.OK).body(payment);
    }

}
