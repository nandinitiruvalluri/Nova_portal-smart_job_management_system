package Springboot.example.SmartJobPortal.repository;

import Springboot.example.SmartJobPortal.entity.Application;
import Springboot.example.SmartJobPortal.entity.Job;
import Springboot.example.SmartJobPortal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByUser(User user);

    boolean existsByUserAndJob(User user, Job job);

    List<Application> findByJobId(Long jobId);

    // Optional: you may remove this if findByJobId is enough
    List<Application> findByJob(Job job);

    Optional<Application> findByUserIdAndJobId(Long userId, Long jobId);

    List<Application> findByUserId(Long userId);
}
