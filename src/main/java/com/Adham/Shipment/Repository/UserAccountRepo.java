package com.Adham.Shipment.Repository;

import com.Adham.Shipment.Shipment.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAccountRepo extends JpaRepository<User, UUID> {

    Optional<User> findOneByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("""
    	SELECT COUNT(u) > 0 FROM User u
    	WHERE u.username = :username
    	""")

    boolean isOwner(@Param("username") String username);

}