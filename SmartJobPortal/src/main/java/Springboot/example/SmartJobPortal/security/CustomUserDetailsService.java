package Springboot.example.SmartJobPortal.security;

import Springboot.example.SmartJobPortal.entity.Admin;
import Springboot.example.SmartJobPortal.entity.User;
import Springboot.example.SmartJobPortal.repository.AdminRepository;
import Springboot.example.SmartJobPortal.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * This method is used by Spring Security by default.
     * It loads regular USERS only.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return new CustomUserDetails(user.getEmail(), user.getPassword(), "ROLE_USER");
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }

    /**
     * Custom method to load ADMINs by email.
     * Called explicitly in JwtAuthFilter when role is ROLE_ADMIN.
     */
    public UserDetails loadAdminByUsername(String email) throws UsernameNotFoundException {
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            return new AdminDetails(admin); // AdminDetails returns ROLE_ADMIN
        }

        throw new UsernameNotFoundException("Admin not found with email: " + email);
    }
}
