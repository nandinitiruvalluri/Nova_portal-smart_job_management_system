package Springboot.example.SmartJobPortal.controller.admin;

import Springboot.example.SmartJobPortal.entity.Application;
import Springboot.example.SmartJobPortal.entity.Job;
//import Springboot.example.SmartJobPortal.entity.Admin;
import Springboot.example.SmartJobPortal.repository.ApplicationRepository;
import Springboot.example.SmartJobPortal.repository.JobRepository;
//import Springboot.example.SmartJobPortal.repository.AdminRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/applications")
@CrossOrigin(origins = "http://localhost:5500")
public class AdminApplicationController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

    //@Autowired
    //private AdminRepository adminRepository;

    // View all applicants for a specific job (only if job belongs to logged-in admin)
    @GetMapping("/job/{jobId}")
    public ResponseEntity<?> getApplicantsForJob(@PathVariable Long jobId, Authentication authentication) {
        String adminEmail = authentication.getName();

        Optional<Job> jobOpt = jobRepository.findById(jobId);
        if (jobOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Job not found");
        }

        Job job = jobOpt.get();

        if (!job.getPostedBy().getEmail().equals(adminEmail)) {
            return ResponseEntity.status(403).body("Forbidden: Cannot view applicants for others' jobs");
        }

        List<Application> applications = applicationRepository.findByJobId(jobId);

        return ResponseEntity.ok(applications);
    }

    // Change application status (Selected / Rejected)
    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<?> updateApplicationStatus(@PathVariable Long applicationId, @RequestParam String status, Authentication authentication) {
        String adminEmail = authentication.getName();

        Optional<Application> applicationOpt = applicationRepository.findById(applicationId);
        if (applicationOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Application not found");
        }

        Application application = applicationOpt.get();

        Job job = application.getJob();
        if (!job.getPostedBy().getEmail().equals(adminEmail)) {
            return ResponseEntity.status(403).body("Forbidden: Cannot update applications for others' jobs");
        }

        if (!status.equalsIgnoreCase("Selected") && !status.equalsIgnoreCase("Rejected")) {
            return ResponseEntity.badRequest().body("Invalid status. Must be 'Selected' or 'Rejected'");
        }

        application.setStatus(status);
        applicationRepository.save(application);

        return ResponseEntity.ok("Application status updated successfully");
    }
}
