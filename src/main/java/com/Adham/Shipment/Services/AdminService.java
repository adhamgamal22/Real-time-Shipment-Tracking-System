package com.Adham.Shipment.Services;

import com.Adham.Shipment.Dto.user.AdminUserResponse;
import com.Adham.Shipment.Dto.user.UpdateRoleRequest;
import com.Adham.Shipment.Repository.ShipmentRepository;
import com.Adham.Shipment.Repository.UserAccountRepo;
import com.Adham.Shipment.Shipment.entites.Shipment;
import com.Adham.Shipment.Shipment.entites.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserAccountRepo userAccountRepo;
    private final ShipmentRepository shipmentRepository;

    public List<AdminUserResponse> getAllUsers() {
	return userAccountRepo.findAll().stream().map(this::mapToResponse).toList();
    }

    public AdminUserResponse updateUserRole(UUID userId, UpdateRoleRequest request) {
	User user = userAccountRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
	user.setRole(request.getRole());
	userAccountRepo.save(user);
	return mapToResponse(user);
    }

    public void deleteUser(UUID userId) {
	User user = userAccountRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
	userAccountRepo.delete(user);
    }

    public List<Shipment> getAllShipments() {
	return shipmentRepository.findAll();
    }

    private AdminUserResponse mapToResponse(User user) {
	return AdminUserResponse.builder().username(user.getUsername()).email(user.getEmail()).phone(user.getPhone())
		.role(user.getRole()).build();
    }
}