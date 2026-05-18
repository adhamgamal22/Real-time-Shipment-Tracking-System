package com.Adham.Shipment.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignupRequest(@NotNull(message = "username is required") @Size(min = 2, max = 50) String username,

	@NotNull(message = "password is required") @Size(min = 2, max = 50) String password,

	@NotNull(message = "email is required") @Email(message = "enter a valid email") String email,

	@NotNull(message = "phone is required") String phone) {
}