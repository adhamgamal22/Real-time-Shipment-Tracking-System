package com.Adham.Shipment.Repository;

import com.Adham.Shipment.Shipment.entites.Payment;
import com.Adham.Shipment.Shipment.entites.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepo extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByShipment(Shipment shipment);
}
