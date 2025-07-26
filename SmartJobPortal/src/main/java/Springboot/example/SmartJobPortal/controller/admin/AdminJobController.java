package Springboot.example.SmartJobPortal.controller.admin;

import Springboot.example.SmartJobPortal.entity.Admin;
import Springboot.example.SmartJobPortal.entity.Job;
import Springboot.example.SmartJobPortal.repository.AdminRepository;
import Springboot.example.SmartJobPortal.repository.JobRepository;
import Springboot.example.SmartJobPortal.dto.JobDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/jobs")
@CrossOrigin(origins = "http://localhost:5500")
public class AdminJobController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private AdminRepository adminRepository;

    // ✅ Helper to convert Job entity to DTO
    private JobDTO mapToDTO(Job job) {
        String postedBy = job.getPostedBy() != null ? job.getPostedBy().getEmail() : null;
        return new JobDTO(
            job.getId(),
            job.getTitle(),
            job.getDescription(),
            job.getCompany(),
            job.getLocation(),
            job.getSalary(),
            postedBy
        );
    }

    // ✅ Get all jobs posted by admin
    @GetMapping
    public ResponseEntity<?> getJobs(Authentication authentication) {
        String adminEmail = authentication.getName();
        Optional<Admin> adminOpt = adminRepository.findByEmail(adminEmail);
        if (adminOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Unauthorized: Admin not found");
        }

        List<Job> jobs = jobRepository.findByPostedById(adminOpt.get().getId());
        List<JobDTO> jobDTOs = jobs.stream().map(this::mapToDTO).collect(Collectors.toList());

        return ResponseEntity.ok(jobDTOs);
    }

    // ✅ Add new job
    @PostMapping
    public ResponseEntity<?> addJob(@RequestBody Job job, Authentication authentication) {
        String adminEmail = authentication.getName();
        Optional<Admin> adminOpt = adminRepository.findByEmail(adminEmail);
        if (adminOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Unauthorized: Admin not found");
        }

        job.setPostedBy(adminOpt.get());
        Job savedJob = jobRepository.save(job);
        return ResponseEntity.ok(mapToDTO(savedJob));
    }

    // ✅ Update job
    @PutMapping("/{jobId}")
    public ResponseEntity<?> updateJob(@PathVariable Long jobId, @RequestBody Job jobDetails, Authentication authentication) {
        String adminEmail = authentication.getName();
        Optional<Admin> adminOpt = adminRepository.findByEmail(adminEmail);
        if (adminOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Unauthorized: Admin not found");
        }

        Optional<Job> jobOpt = jobRepository.findById(jobId);
        if (jobOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Job not found");
        }

        Job job = jobOpt.get();
        if (!job.getPostedBy().getEmail().equals(adminEmail)) {
            return ResponseEntity.status(403).body("Forbidden: Cannot update others' jobs");
        }

        job.setTitle(jobDetails.getTitle());
        job.setDescription(jobDetails.getDescription());
        job.setLocation(jobDetails.getLocation());
        job.setCompany(jobDetails.getCompany());
        job.setSalary(jobDetails.getSalary());

        Job updatedJob = jobRepository.save(job);
        return ResponseEntity.ok(mapToDTO(updatedJob));
    }

    // ✅ Delete job
    @DeleteMapping("/{jobId}")
    public ResponseEntity<?> deleteJob(@PathVariable Long jobId, Authentication authentication) {
        String adminEmail = authentication.getName();
        Optional<Admin> adminOpt = adminRepository.findByEmail(adminEmail);
        if (adminOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Unauthorized: Admin not found");
        }

        Optional<Job> jobOpt = jobRepository.findById(jobId);
        if (jobOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Job not found");
        }

        Job job = jobOpt.get();
        if (!job.getPostedBy().getEmail().equals(adminEmail)) {
            return ResponseEntity.status(403).body("Forbidden: Cannot delete others' jobs");
        }

        jobRepository.delete(job);
        return ResponseEntity.ok("Job deleted successfully");
    }
}
