package com.Adham.Shipment.Dto.user;

import com.Adham.Shipment.Shipment.entites.Role;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AdminUserResponse {
    private UUID id;
    private String username;
    private String email;
    private String phone;
    private Role role;
}