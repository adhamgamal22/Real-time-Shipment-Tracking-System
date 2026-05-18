package com.Adham.Shipment.Shipment.entites;

import com.Adham.Shipment.Shipment.entites.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Shipment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID shipmentId;

    @Column(nullable = false, unique = true)
    private String trackingNumber;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShipmentStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private String estimatedDeliveryDate;

    private Double currentLatitude;
    private Double currentLongitude;
    private String currentLocation;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @PrePersist
    protected void onCreate() {
	this.createdAt = LocalDateTime.now();
	updatedAt = LocalDateTime.now();
	if (status == null) {

	    status = ShipmentStatus.ORDER_PLACED;

	}

    }

    @PreUpdate
    protected void onUpdate() {
	updatedAt = LocalDateTime.now();
    }

}
