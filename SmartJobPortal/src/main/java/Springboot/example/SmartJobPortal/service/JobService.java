package Springboot.example.SmartJobPortal.service;

import Springboot.example.SmartJobPortal.entity.Job;
import Springboot.example.SmartJobPortal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    // Create or update job
    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    // Get all jobs
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // Get jobs by admin id (who posted the jobs)
    public List<Job> getJobsByAdminId(Long adminId) {
        return jobRepository.findByPostedById(adminId);
    }

    // Find job by id
    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    // Delete job by id
    public void deleteJobById(Long id) {
        jobRepository.deleteById(id);
    }
}
