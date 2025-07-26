package Springboot.example.SmartJobPortal.dto;

public class ApplicationRequest {

    private Long jobId;
    private String resume;
    private String coverLetter;

    public ApplicationRequest() {}

    public ApplicationRequest(Long jobId, String resume, String coverLetter) {
        this.jobId = jobId;
        this.resume = resume;
        this.coverLetter = coverLetter;
    }

    // Getters and Setters
    public Long getJobId() {
        return jobId;
    }
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getResume() {
        return resume;
    }
    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getCoverLetter() {
        return coverLetter;
    }
    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }
}
