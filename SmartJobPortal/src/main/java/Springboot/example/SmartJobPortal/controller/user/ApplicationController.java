package Springboot.example.SmartJobPortal.controller.user;

import Springboot.example.SmartJobPortal.entity.Application;
import Springboot.example.SmartJobPortal.entity.Job;
import Springboot.example.SmartJobPortal.entity.User;
import Springboot.example.SmartJobPortal.repository.ApplicationRepository;
import Springboot.example.SmartJobPortal.repository.JobRepository;
import Springboot.example.SmartJobPortal.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/applications")
//@CrossOrigin(origins = "http://localhost:5500")

public class ApplicationController {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public ApplicationController(ApplicationRepository applicationRepository,
                                 UserRepository userRepository,
                                 JobRepository jobRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    @Value("${upload.dir}")
    private String uploadDir;

    @PostMapping("/apply")
    public ResponseEntity<?> applyToJob(
            @RequestParam("jobId") Long jobId,
            @RequestParam("fullName") String fullName,
            @RequestParam("resume") MultipartFile resumeFile
    ) {
        try {
            // 🔐 Get authenticated user's email
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            Optional<User> optionalUser = userRepository.findByEmail(email);
            Optional<Job> optionalJob = jobRepository.findById(jobId)
                    .filter(job -> job.getPostedBy() != null); // ✅ Check job has valid admin

            if (optionalUser.isEmpty() || optionalJob.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid user or job.");
            }

            User user = optionalUser.get();
            Job job = optionalJob.get();

            //  Check if user already applied
            if (applicationRepository.existsByUserAndJob(user, job)) {
                return ResponseEntity.status(409).body("Already applied.");
            }

            // ✅ Validate resume format
            String originalFilename = resumeFile.getOriginalFilename();
            if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".pdf")) {
                return ResponseEntity.badRequest().body("Only PDF resumes allowed.");
            }

            // ✅ Ensure upload folder exists
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) {
                boolean created = uploadPath.mkdirs();
                if (!created) {
                    return ResponseEntity.status(500).body("Failed to create upload directory.");
                }
            }

            // ✅ Save resume file
            String cleanedFilename = UUID.randomUUID() + "-" + StringUtils.cleanPath(originalFilename);
            File destinationFile = new File(uploadPath, cleanedFilename);
            resumeFile.transferTo(destinationFile);

            // 💾 Save application in DB
            Application application = new Application();
            application.setUser(user);
            application.setJob(job);
            application.setStatus("PENDING");
            application.setAppliedDate(LocalDate.now());
            application.setResumeFilename(cleanedFilename);

            applicationRepository.save(application);

            return ResponseEntity.ok("✅ Application submitted successfully. Resume saved as: " + cleanedFilename);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("❌ Resume upload failed.");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("❌ Unexpected error: " + ex.getMessage());
        }
    }

    @GetMapping("/user/status")
    public ResponseEntity<?> getUserApplications() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid user.");
        }

        List<Application> apps = applicationRepository.findByUser(user.get());
        return ResponseEntity.ok(apps);
    }
}
