package com.Adham.Shipment.Services;

import com.Adham.Shipment.Dto.LoginRequest;
import com.Adham.Shipment.Dto.SignupRequest;
import com.Adham.Shipment.Repository.PasswordResetRepo;
import com.Adham.Shipment.Repository.UserAccountRepo;
import com.Adham.Shipment.Shared.CustomResponseException;
import com.Adham.Shipment.Shipment.entites.PasswordReset;
import com.Adham.Shipment.Shipment.entites.Role;
import com.Adham.Shipment.Shipment.entites.User;
import com.Adham.Shipment.config.JwtHelper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private UserAccountRepo userAccountRepo;
    private PasswordResetRepo passwordRestRepo;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtHelper jwtHelper;

    public void signup(SignupRequest signupRequest) {
	if (userAccountRepo.findOneByUsername(signupRequest.username()).isPresent()) {
	    throw CustomResponseException.BadRequest("Username already exists");
	}
	User user = User.builder().username(signupRequest.username())
		.password(passwordEncoder.encode(signupRequest.password())).verified(true).role(Role.USER).build();

	userAccountRepo.save(user);
    }

    public String login(LoginRequest loginRequest) {
	authenticationManager.authenticate(
		new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

	User user = userAccountRepo.findOneByUsername(loginRequest.username())
		.orElseThrow(CustomResponseException::BadCredentials);

	Map<String, Object> customClaims = new HashMap<>();
	customClaims.put("userId", user.getUserid());
	customClaims.put("role", user.getRole());
	return jwtHelper.generateToken(customClaims, user);
    }

    @Transactional
    public String initiatePasswordReset(String username) {
	try {
	    User user = userAccountRepo.findOneByUsername(username)
		    .orElseThrow(() -> CustomResponseException.ResourceNotFound("Account not found"));

	    String token = UUID.randomUUID().toString();
	    LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);

	    PasswordReset reset = new PasswordReset();
	    reset.setToken(token);
	    reset.setExpiryDate(expiry);
	    reset.setUser(user);
	    passwordRestRepo.save(reset);

	    return token;

	} catch (Exception e) {
	    throw CustomResponseException.BadRequest("Failed to initiate password reset");
	}
    }
}