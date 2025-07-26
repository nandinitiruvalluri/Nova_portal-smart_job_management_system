package Springboot.example.SmartJobPortal.controller.user;

import Springboot.example.SmartJobPortal.entity.User;
import Springboot.example.SmartJobPortal.repository.AdminRepository;
import Springboot.example.SmartJobPortal.repository.UserRepository;
import Springboot.example.SmartJobPortal.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:5500")
public class UserAuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    // ✅ Register new user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();

        if (userRepository.existsByEmail(user.getEmail())) {
            response.put("message", "Email already has an account as User");
            return ResponseEntity.badRequest().body(response);
        }

        if (adminRepository.existsByEmail(user.getEmail())) {
            response.put("message", "Email already exists as Admin");
            return ResponseEntity.badRequest().body(response);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        response.put("message", "User registered successfully");
        return ResponseEntity.ok(response);
    }

    // ✅ Login user and return JWT token
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData) {
        Map<String, String> response = new HashMap<>();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginData.get("email"),
                            loginData.get("password")
                    )
            );

            // ✅ Generate JWT Token and return to frontend
            User user = userRepository.findByEmail(loginData.get("email"))
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtService.generateToken(user.getEmail(), "ROLE_USER");

            response.put("message", "Login successful");
            response.put("token", token);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Invalid email or password. Please try again.");
            return ResponseEntity.status(401).body(response);
        }
    }
}
