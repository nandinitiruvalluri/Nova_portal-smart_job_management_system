package Springboot.example.SmartJobPortal.controller.user;

import Springboot.example.SmartJobPortal.entity.User;
import Springboot.example.SmartJobPortal.repository.AdminRepository;
import Springboot.example.SmartJobPortal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
//@CrossOrigin(origins = "http://localhost:5500")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Registration method
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();

        if (userRepository.existsByEmail(user.getEmail())) {
            response.put("message", "Email already exists as User");
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

    // ✅ Profile endpoint to return logged-in user's name (used in apply-job.html)
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();

        if (userDetails == null || userDetails.getUsername() == null) {
            response.put("error", "User not authenticated");
            return ResponseEntity.status(401).body(response);
        }

        Optional<User> optionalUser = userRepository.findByEmail(userDetails.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            response.put("name", user.getName());
            response.put("email", user.getEmail());
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "User not found");
            return ResponseEntity.status(404).body(response);
        }
    }
}
