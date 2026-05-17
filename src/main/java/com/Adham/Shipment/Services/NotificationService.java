package com.Adham.Shipment.Services;

import com.Adham.Shipment.Dto.NotificationResponse;
import com.Adham.Shipment.Repository.NotificationRepo;
import com.Adham.Shipment.Repository.UserAccountRepo;
import com.Adham.Shipment.Shipment.entites.Notification;
import com.Adham.Shipment.Shipment.entites.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepo notificationRepo;
    private final UserAccountRepo userAccountRepo;
    private final SimpMessagingTemplate messagingTemplate; // ده اللي بيبعت الـ WebSocket

    private User getCurrentUser() {
	String email = SecurityContextHolder.getContext().getAuthentication().getName();
	return userAccountRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    private NotificationResponse mapToResponse(Notification n) {
	return NotificationResponse.builder().id(n.getId()).message(n.getMessage()).isRead(n.isRead())
		.createdAt(n.getCreatedAt()).build();
    }

    public void sendNotification(User user, String message) {
	Notification notification = new Notification();
	notification.setUser(user);
	notification.setMessage(message);
	notificationRepo.save(notification);

	messagingTemplate.convertAndSendToUser(user.getEmail(), "/queue/notifications", mapToResponse(notification));
    }

    public List<NotificationResponse> getAllNotifications() {
	User user = getCurrentUser();
	return notificationRepo.findByUserOrderByCreatedAtDesc(user).stream().map(this::mapToResponse).toList();
    }

    public List<NotificationResponse> getUnreadNotifications() {
	User user = getCurrentUser();
	return notificationRepo.findByUserAndIsReadFalse(user).stream().map(this::mapToResponse).toList();
    }

    public void markAllAsRead() {
	User user = getCurrentUser();
	List<Notification> unread = notificationRepo.findByUserAndIsReadFalse(user);
	unread.forEach(n -> n.setRead(true));
	notificationRepo.saveAll(unread);
    }
}