package com.Adham.Shipment.Dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NotificationResponse {
    private UUID id;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;
}