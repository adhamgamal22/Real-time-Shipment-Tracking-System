package com.Adham.Shipment.Services;

import com.Adham.Shipment.Repository.UserAccountRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserAccountRepo userAccountRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	return userAccountRepo.findOneByUsername(username)
		.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public UserDetails updatePassword(UserDetails user, String newPassword) {

	return user;
    }
}
