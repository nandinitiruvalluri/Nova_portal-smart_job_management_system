package Springboot.example.SmartJobPortal.repository;

import Springboot.example.SmartJobPortal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // ✅ Find a user by their email (used in login and security)
    Optional<User> findByEmail(String email);

    // ✅ Check if a user exists with the given email (for registration)
    boolean existsByEmail(String email);
}
