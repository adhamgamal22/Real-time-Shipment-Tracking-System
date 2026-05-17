package com.Adham.Shipment.controller;

import com.Adham.Shipment.Dto.user.AdminUserResponse;
import com.Adham.Shipment.Dto.user.UpdateRoleRequest;
import com.Adham.Shipment.Services.AdminService;
import com.Adham.Shipment.Shipment.entites.Shipment;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Admin Management APIs")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponse>> getAllUsers() {
	return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<AdminUserResponse> updateUserRole(@PathVariable UUID userId,
	    @RequestBody @Valid UpdateRoleRequest request) {
	return ResponseEntity.ok(adminService.updateUserRole(userId, request));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
	adminService.deleteUser(userId);
	return ResponseEntity.noContent().build();
    }

    @GetMapping("/shipments")
    public ResponseEntity<List<Shipment>> getAllShipments() {
	return ResponseEntity.ok(adminService.getAllShipments());
    }
}
