package Springboot.example.SmartJobPortal.dto;

public class ApplicationDTO {
    private Long id;
    private String status;
    private String messageToApplicant;
    private String userName;
    private String userEmail;
    private String jobTitle;

    public ApplicationDTO(Long id, String status, String messageToApplicant,
                          String userName, String userEmail, String jobTitle) {
        this.id = id;
        this.status = status;
        this.messageToApplicant = messageToApplicant;
        this.userName = userName;
        this.userEmail = userEmail;
        this.jobTitle = jobTitle;
    }

    // Getters
    public Long getId() { return id; }
    public String getStatus() { return status; }
    public String getMessageToApplicant() { return messageToApplicant; }
    public String getUserName() { return userName; }
    public String getUserEmail() { return userEmail; }
    public String getJobTitle() { return jobTitle; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setStatus(String status) { this.status = status; }
    public void setMessageToApplicant(String messageToApplicant) { this.messageToApplicant = messageToApplicant; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
}
