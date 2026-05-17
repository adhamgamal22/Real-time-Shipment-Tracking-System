package com.Adham.Shipment.controller;

import com.Adham.Shipment.Dto.LoginRequest;
import com.Adham.Shipment.Dto.SignupRequest;
import com.Adham.Shipment.Services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "auth", description = "authentication ")
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
	String token = authService.login(loginRequest);
	System.out.println("Login attempt for user: " + loginRequest.username());
	return ResponseEntity.ok(token);
    }

    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
	authService.signup(signupRequest);
	return ResponseEntity.ok("Account created successfully");
    }

    @PostMapping("reset-password/initiate")
    public ResponseEntity<String> initiatePasswordReset(@RequestParam String username) {
	String token = authService.initiatePasswordReset(username);
	return ResponseEntity.ok(token);
    }
}
