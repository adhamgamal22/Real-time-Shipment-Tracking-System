package com.Adham.Shipment.controller;

import com.Adham.Shipment.Dto.user.ChangePasswordRequest;
import com.Adham.Shipment.Dto.user.UpdateProfileRequest;
import com.Adham.Shipment.Dto.user.UserProfileResponse;
import com.Adham.Shipment.Services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User", description = "User Profile")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile() {
	return ResponseEntity.ok(userService.getProfile());
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateProfile(@RequestBody @Valid UpdateProfileRequest request) {
	return ResponseEntity.ok(userService.updateProfile(request));
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
	userService.changePassword(request);
	return ResponseEntity.ok().build();
    }
}
