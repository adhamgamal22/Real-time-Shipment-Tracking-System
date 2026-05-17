package com.Adham.Shipment.Dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponse {
    private String username;
    private String email;
    private String phone;
}