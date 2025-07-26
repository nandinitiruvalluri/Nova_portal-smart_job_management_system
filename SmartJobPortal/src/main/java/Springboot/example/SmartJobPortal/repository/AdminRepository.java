package Springboot.example.SmartJobPortal.repository;

import Springboot.example.SmartJobPortal.entity.Admin;
//import Springboot.example.SmartJobPortal.entity.Application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
//import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Find admin by email for login and authentication
    Optional<Admin> findByEmail(String email);

    // Check if an admin with given email already exists (for registration)
    boolean existsByEmail(String email);
    //List<Application> findByJobId(Long jobId);

}
