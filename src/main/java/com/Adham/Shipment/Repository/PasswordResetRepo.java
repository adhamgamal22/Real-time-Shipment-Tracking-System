package com.Adham.Shipment.Repository;

import com.Adham.Shipment.Shipment.entites.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetRepo extends JpaRepository<PasswordReset, UUID> {

    Optional<PasswordReset> findByToken(String token);
}
