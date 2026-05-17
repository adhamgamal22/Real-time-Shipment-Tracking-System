package com.Adham.Shipment.Repository;

import com.Adham.Shipment.Shipment.entites.Payment;
import com.Adham.Shipment.Shipment.entites.Shipment;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {

    Optional<Shipment> findByTrackingNumber(String trackingnumber);

    Optional<Shipment> findById(UUID shipmentId);
}
