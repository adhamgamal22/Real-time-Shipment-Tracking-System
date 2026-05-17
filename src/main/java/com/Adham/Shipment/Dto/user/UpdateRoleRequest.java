package com.Adham.Shipment.Dto.user;

import com.Adham.Shipment.Shipment.entites.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRoleRequest {

    private Role role;
}