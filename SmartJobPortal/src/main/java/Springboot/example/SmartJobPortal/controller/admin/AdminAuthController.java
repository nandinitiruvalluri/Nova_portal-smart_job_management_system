package Springboot.example.SmartJobPortal.controller.admin;

import Springboot.example.SmartJobPortal.entity.Admin;
import Springboot.example.SmartJobPortal.repository.AdminRepository;
import Springboot.example.SmartJobPortal.repository.UserRepository;
import Springboot.example.SmartJobPortal.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/auth")
//@CrossOrigin(origins = "http://localhost:5500")
public class AdminAuthController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    // ✅ Admin Register
    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody Admin admin) {
        if (adminRepository.existsByEmail(admin.getEmail())) {
            return ResponseEntity.badRequest().body(
                Map.of("message", "Email already has an account as Admin")
            );
        }

        if (userRepository.existsByEmail(admin.getEmail())) {
            return ResponseEntity.badRequest().body(
                Map.of("message", "Email already exists as User")
            );
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminRepository.save(admin);

        return ResponseEntity.ok(
            Map.of(
                "status", "success",
                "message", "Admin registered successfully"
            )
        );
    }

    // ✅ Admin Login with JWT
    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        Optional<Admin> optionalAdmin = adminRepository.findByEmail(email);

        if (optionalAdmin.isEmpty()) {
            return ResponseEntity.status(401).body(
                Map.of("message", "Invalid email or password")
            );
        }

        Admin admin = optionalAdmin.get();

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            return ResponseEntity.status(401).body(
                Map.of("message", "Invalid email or password")
            );
        }

        String token = jwtService.generateToken(admin.getEmail(), "ROLE_ADMIN");


        return ResponseEntity.ok(
            Map.of(
                "token", token,
                "email", admin.getEmail(),
                "name", admin.getName(),
                "role", admin.getRole()
            )
        );
    }
}
