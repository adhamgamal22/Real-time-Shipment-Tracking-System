package com.Adham.Shipment.Utils;

import com.Adham.Shipment.Repository.UserAccountRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class SecurityUtils {

    @Autowired
    private UserAccountRepo userAccountRepo;

    public boolean isOwner(UUID incomingshipmentId) {
	final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	System.out.println(userDetails.getUsername());
	System.out.println("HERE " + incomingshipmentId);

	return userAccountRepo.isOwner(userDetails.getUsername());
    }
}