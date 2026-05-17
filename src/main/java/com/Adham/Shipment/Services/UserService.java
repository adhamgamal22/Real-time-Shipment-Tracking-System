package com.Adham.Shipment.Services;

import com.Adham.Shipment.Dto.user.ChangePasswordRequest;
import com.Adham.Shipment.Dto.user.UpdateProfileRequest;
import com.Adham.Shipment.Dto.user.UserProfileResponse;
import com.Adham.Shipment.Shipment.entites.User;
import com.Adham.Shipment.Repository.UserAccountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserAccountRepo userAccountRepo;
    private final PasswordEncoder passwordEncoder;

    private User getCurrentUser() {
	String email = SecurityContextHolder.getContext().getAuthentication().getName();
	return userAccountRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserProfileResponse getProfile() {
	User user = getCurrentUser();
	return UserProfileResponse.builder().username(user.getUsername()).email(user.getEmail()).phone(user.getPhone())
		.build();
    }

    public UserProfileResponse updateProfile(UpdateProfileRequest request) {
	User user = getCurrentUser();
	user.setUsername(request.getUsername());
	user.setPhone(request.getPhone());
	userAccountRepo.save(user);
	return UserProfileResponse.builder().username(user.getUsername()).email(user.getEmail()).phone(user.getPhone())
		.build();
    }

    public void changePassword(ChangePasswordRequest request) {
	User user = getCurrentUser();
	if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
	    throw new RuntimeException("Current password is incorrect");
	}
	user.setPassword(passwordEncoder.encode(request.getNewPassword()));
	userAccountRepo.save(user);
    }
}