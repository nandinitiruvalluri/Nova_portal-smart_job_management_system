package Springboot.example.SmartJobPortal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Job description is required")
    @Column(length = 1000)
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Company name is required")
    private String company;

    @NotBlank(message = "Salary is required")
    private String salary;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    @JsonIgnoreProperties({"jobs", "password"})
    private Admin postedBy;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Application> applications;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }

    public Admin getPostedBy() { return postedBy; }
    public void setPostedBy(Admin postedBy) { this.postedBy = postedBy; }

    public List<Application> getApplications() { return applications; }
    public void setApplications(List<Application> applications) { this.applications = applications; }
}
