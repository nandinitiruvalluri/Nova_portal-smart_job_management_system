package Springboot.example.SmartJobPortal.service.user;

import Springboot.example.SmartJobPortal.entity.Application;
import Springboot.example.SmartJobPortal.entity.Job;
import Springboot.example.SmartJobPortal.entity.User;
import Springboot.example.SmartJobPortal.repository.ApplicationRepository;
import Springboot.example.SmartJobPortal.repository.JobRepository;
import Springboot.example.SmartJobPortal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    // ✅ User applies to a job
    public Application applyToJob(String userEmail, Long jobId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Prevent multiple applications to same job
        if (applicationRepository.findByUserIdAndJobId(user.getId(), jobId) != null) {
            throw new RuntimeException("You already applied to this job");
        }

        Application application = new Application();
        application.setUser(user);
        application.setJob(job);
        application.setStatus("Applied");

        return applicationRepository.save(application);
    }

    // ✅ User: Get own applications
    public List<Application> getApplicationsByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return applicationRepository.findByUserId(user.getId());
    }

    // ✅ Admin: Get applicants for a job
    public List<Application> getApplicationsForJob(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }

    // ✅ Admin: Update application status
    public Application updateApplicationStatus(Long applicationId, String status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setStatus(status);
        return applicationRepository.save(application);
    }
}
