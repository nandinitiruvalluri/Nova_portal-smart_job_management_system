package Springboot.example.SmartJobPortal.service.admin;

import Springboot.example.SmartJobPortal.entity.Admin;
import Springboot.example.SmartJobPortal.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register new admin (check duplicate email first)
    public Admin registerAdmin(Admin admin) throws IllegalArgumentException {
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }
        // Encrypt password before saving
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    // Login admin by email and password
    public Admin loginAdmin(String email, String password) throws IllegalArgumentException {
        Optional<Admin> optionalAdmin = adminRepository.findByEmail(email);
        if (optionalAdmin.isEmpty()) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        Admin admin = optionalAdmin.get();

        // Use password encoder match instead of plain equals
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return admin;
    }

    // Find admin by id
    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }
}
