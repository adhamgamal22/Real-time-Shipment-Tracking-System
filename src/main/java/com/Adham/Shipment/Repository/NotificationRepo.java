package com.Adham.Shipment.Repository;

import com.Adham.Shipment.Shipment.entites.Notification;
import com.Adham.Shipment.Shipment.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, UUID> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    List<Notification> findByUserAndIsReadFalse(User user);
}
