package com.Adham.Shipment.Dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String phone;
}