package com.Adham.Shipment.config;

import com.Adham.Shipment.Services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthFilter jwtAuthFilter;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImp;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsServiceImpl userDetailsService) {
	this.jwtAuthFilter = jwtAuthFilter;
	this.userDetailsServiceImp = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	http.csrf(csrf -> csrf.disable()).cors(Customizer.withDefaults())
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/**").permitAll()
			.requestMatchers("/auth/login", "/auth/signup", "/auth/reset-password/**").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/shipment/getall").hasRole("USER")
			.requestMatchers(HttpMethod.GET, "/api/shipment/id").hasAnyRole("ADMIN", "USER")
			.requestMatchers(HttpMethod.GET, "/payment/getall").hasAnyRole("ADMIN")
			.requestMatchers(HttpMethod.GET, "/api/shipment/track/{trackingnumber}")
			.hasAnyRole("ADMIN", "USER").requestMatchers(HttpMethod.DELETE, "/api/shipments/**")
			.hasRole("ADMIN").requestMatchers(HttpMethod.POST, "/payment/process").authenticated()
			.requestMatchers(HttpMethod.POST, "/api/shipment/create").hasAnyRole("ADMIN", "USER")
			.requestMatchers(HttpMethod.GET, "/user/**").hasRole("USER")
			.requestMatchers(HttpMethod.PUT, "/user/**").hasRole("USER").requestMatchers("/admin/**")
			.hasRole("ADMIN").requestMatchers("/user/**").hasRole("USER").requestMatchers("/payment/**")
			.authenticated().requestMatchers("/api/shipment/**").authenticated()
			.requestMatchers(HttpMethod.PUT, "/payment/{trackingNumber}/cancle").authenticated()
			.requestMatchers(HttpMethod.POST, "/shipment/{shipmentId}").authenticated()
			.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
			.requestMatchers("/location/**").authenticated().requestMatchers("/stripe/webhook").permitAll()
			.requestMatchers("/stripe/**").authenticated().anyRequest().authenticated())
		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
	return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
	DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsServiceImp);
	provider.setPasswordEncoder(passwordEncoder());
	return new ProviderManager(provider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
    }
}