package com.Adham.Shipment.Shipment.entites;

import jakarta.persistence.*;

import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor

public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shipment_id", nullable = false, unique = true)
    private Shipment shipment;

    @PrePersist
    protected void OnCreated() {
	this.createdAt = LocalDateTime.now();
	this.updatedAt = LocalDateTime.now();
	if (paymentStatus == null) {
	    paymentStatus = PaymentStatus.PENDING;

	}

    }

}
