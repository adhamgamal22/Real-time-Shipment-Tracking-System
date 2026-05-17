package com.Adham.Shipment.Shipment.entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users_table")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @Size(min = 8)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Email
    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "account_creation_token")
    private String accountCreationToken;

    private boolean verified = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
	return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Shipment> shipments;

    public void setAccountCreationToken(Object o) {

	this.accountCreationToken = (String) o;
    }

}
