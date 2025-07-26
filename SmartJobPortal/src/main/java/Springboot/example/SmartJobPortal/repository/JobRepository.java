package Springboot.example.SmartJobPortal.repository;

import Springboot.example.SmartJobPortal.entity.Admin;
import Springboot.example.SmartJobPortal.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    // 🔹 Find all jobs posted by a particular admin (using ID)
    List<Job> findByPostedById(Long adminId);

    // 🔹 Find all jobs posted by a particular admin (using Admin entity)
    List<Job> findByPostedBy(Admin admin);

    // 🔹 Find specific job by ID and admin ID
    Optional<Job> findByIdAndPostedById(Long jobId, Long adminId);

    // ✅ NEW: Find only valid jobs (for public listing)
    @Query("SELECT j FROM Job j WHERE " +
           "j.title IS NOT NULL AND j.title <> '' AND " +
           "j.location IS NOT NULL AND j.location <> '' AND " +
           "j.salary IS NOT NULL AND j.salary <> '' AND " +
           "j.company IS NOT NULL AND j.company <> ''")
    List<Job> findValidJobs();
}
