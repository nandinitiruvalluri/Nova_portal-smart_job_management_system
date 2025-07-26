package Springboot.example.SmartJobPortal.controller.admin;

import Springboot.example.SmartJobPortal.dto.ApplicationDTO;
import Springboot.example.SmartJobPortal.entity.Admin;
import Springboot.example.SmartJobPortal.entity.Application;
import Springboot.example.SmartJobPortal.entity.Job;
import Springboot.example.SmartJobPortal.repository.AdminRepository;
import Springboot.example.SmartJobPortal.repository.ApplicationRepository;
import Springboot.example.SmartJobPortal.repository.JobRepository;
import Springboot.example.SmartJobPortal.security.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
//import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5500")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JwtService jwtService;

    // 🔐 Helper to extract email from JWT
    private String extractEmail(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid token");
        }
        return jwtService.extractUsername(authHeader.substring(7));
    }

    // 🔐 Helper to fetch admin
    private Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    // ✅ Admin profile
    @GetMapping("/profile")
    public ResponseEntity<?> getAdminProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            String email = extractEmail(authHeader);
            Admin admin = getAdminByEmail(email);
            return ResponseEntity.ok(Map.of("email", admin.getEmail(), "name", admin.getName()));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }

    // ✅ All applications for admin’s jobs (returns ApplicationDTOs)
    @GetMapping("/applications")
    public ResponseEntity<?> getAllApplications(@RequestHeader("Authorization") String authHeader) {
        try {
            String email = extractEmail(authHeader);
            Admin admin = getAdminByEmail(email);
            List<Job> jobs = jobRepository.findByPostedBy(admin);

            List<ApplicationDTO> result = new ArrayList<>();
            for (Job job : jobs) {
                List<Application> apps = applicationRepository.findByJob(job);
                for (Application app : apps) {
                    result.add(new ApplicationDTO(
                        app.getId(),
                        app.getStatus(),
                        app.getMessageToApplicant(),
                        app.getUser().getName(),
                        app.getUser().getEmail(),
                        job.getTitle()
                    ));
                }
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }

    // ✅ Applications for specific job (still raw Application entity)
    @GetMapping("/jobs/{jobId}/applications")
    public ResponseEntity<?> getApplicationsForJob(@PathVariable Long jobId,
                                                   @RequestHeader("Authorization") String authHeader) {
        try {
            String email = extractEmail(authHeader);
            Admin admin = getAdminByEmail(email);

            Job job = jobRepository.findByIdAndPostedById(jobId, admin.getId())
                    .orElseThrow(() -> new RuntimeException("Job not found"));

            return ResponseEntity.ok(applicationRepository.findByJob(job));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }

    // ✅ Update application status
    @PutMapping("/applications/{applicationId}/status")
    public ResponseEntity<?> updateApplicationStatus(@PathVariable Long applicationId,
                                                     @RequestBody Map<String, String> request,
                                                     @RequestHeader("Authorization") String authHeader) {
        try {
            String email = extractEmail(authHeader);
            Admin admin = getAdminByEmail(email);

            Application app = applicationRepository.findById(applicationId)
                    .orElseThrow(() -> new RuntimeException("Application not found"));

            if (!app.getJob().getPostedBy().getId().equals(admin.getId())) {
                return ResponseEntity.status(403).body(Map.of("message", "Unauthorized action"));
            }

            app.setStatus(request.get("status"));
            if (request.containsKey("message")) {
                app.setMessageToApplicant(request.get("message"));
            }

            applicationRepository.save(app);
            return ResponseEntity.ok(Map.of("message", "Status updated"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("message", e.getMessage()));
        }
    }
}  
