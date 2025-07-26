package Springboot.example.SmartJobPortal.controller;

import Springboot.example.SmartJobPortal.entity.Admin;
import Springboot.example.SmartJobPortal.entity.Job;
import Springboot.example.SmartJobPortal.repository.AdminRepository;
import Springboot.example.SmartJobPortal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "http://localhost:5500")
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping
    public ResponseEntity<List<Job>> getAllValidJobs() {
        List<Job> jobs = jobRepository.findValidJobs();
        List<Job> validJobs = jobs.stream()
                .filter(job -> job.getPostedBy() != null)
                .collect(Collectors.toList());
        return ResponseEntity.ok(validJobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return jobRepository.findById(id)
                .filter(job -> job.getPostedBy() != null)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> addJob(@RequestBody Job job, Authentication authentication) {
        System.out.println("\uD83D\uDCC5 Received job data: " + job);
        System.out.println("\uD83D\uDC64 Authenticated Admin Email: " + authentication.getName());

        String email = authentication.getName();
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        job.setPostedBy(admin);
        Job savedJob = jobRepository.save(job);

        System.out.println("\u2705 Job saved with ID: " + savedJob.getId());
        return ResponseEntity.ok(savedJob);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @RequestBody Job jobDetails, Authentication authentication) {
        Job job = jobRepository.findById(id).orElse(null);
        if (job == null) {
            return ResponseEntity.notFound().build();
        }

        String email = authentication.getName();
        if (!job.getPostedBy().getEmail().equals(email)) {
            return ResponseEntity.status(403).body("Access Denied: Not your job posting");
        }

        job.setTitle(jobDetails.getTitle());
        job.setDescription(jobDetails.getDescription());
        job.setLocation(jobDetails.getLocation());
        job.setCompany(jobDetails.getCompany());
        job.setSalary(jobDetails.getSalary());

        Job updatedJob = jobRepository.save(job);
        return ResponseEntity.ok(updatedJob);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id, Authentication authentication) {
        Job job = jobRepository.findById(id).orElse(null);
        if (job == null) {
            return ResponseEntity.notFound().build();
        }

        String email = authentication.getName();
        if (!job.getPostedBy().getEmail().equals(email)) {
            return ResponseEntity.status(403).body("Access Denied: Not your job posting");
        }

        jobRepository.delete(job);
        return ResponseEntity.ok("Job deleted successfully");
    }
}
