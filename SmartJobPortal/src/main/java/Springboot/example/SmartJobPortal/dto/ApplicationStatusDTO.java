package Springboot.example.SmartJobPortal.dto;

import java.time.LocalDateTime;

public class ApplicationStatusDTO {
    private Long applicationId;
    private String jobTitle;
    private String jobLocation;
    private String recruiterName;
    private String status;
    private LocalDateTime appliedDate;

    public ApplicationStatusDTO(Long applicationId, String jobTitle, String jobLocation,
                                String recruiterName, String status, LocalDateTime appliedDate) {
        this.applicationId = applicationId;
        this.jobTitle = jobTitle;
        this.jobLocation = jobLocation;
        this.recruiterName = recruiterName;
        this.status = status;
        this.appliedDate = appliedDate;
    }

    // Getters and setters

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getRecruiterName() {
        return recruiterName;
    }

    public void setRecruiterName(String recruiterName) {
        this.recruiterName = recruiterName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDateTime appliedDate) {
        this.appliedDate = appliedDate;
    }
}
