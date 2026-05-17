package com.Adham.Shipment.controller;

import com.Adham.Shipment.Dto.NotificationResponse;
import com.Adham.Shipment.Services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
@Tag(name = "Notifications", description = "Notification For APP")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAll() {
	return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnread() {
	return ResponseEntity.ok(notificationService.getUnreadNotifications());
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead() {
	notificationService.markAllAsRead();
	return ResponseEntity.ok().build();
    }
}