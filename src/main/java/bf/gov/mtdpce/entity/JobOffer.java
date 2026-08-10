package bf.gov.mtdpce.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_offers")
public class JobOffer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 255)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "contract_type", length = 50)
    private String contractType;
    
    @Column(length = 100)
    private String department;
    
    @Column(length = 255)
    private String location;
    
    private LocalDate deadline;
    
    @Column(columnDefinition = "TEXT")
    private String requirements;
    
    @Column(columnDefinition = "TEXT")
    private String responsibilities;
    
    @Column(columnDefinition = "TEXT")
    private String qualifications;
    
    @Column(length = 100)
    private String experience;
    
    @Column(length = 100)
    private String salary;
    
    @Column(name = "application_email", length = 255)
    private String applicationEmail;
    
    @Column(name = "application_url", length = 500)
    private String applicationUrl;
    
    @Column(name = "application_instructions", columnDefinition = "TEXT")
    private String applicationInstructions;
    
    @Column(name = "required_documents", columnDefinition = "TEXT")
    private String requiredDocuments;
    
    @Column(name = "number_of_positions")
    private Integer numberOfPositions = 1;
    
    @Column(name = "reference_number", length = 50)
    private String referenceNumber;
    
    @Column(name = "is_published")
    private Boolean isPublished = true;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private JobOfferStatus status = JobOfferStatus.OPEN;
    
    @Column(name = "application_count")
    private Long applicationCount = 0L;
    
    @Column(name = "view_count")
    private Long viewCount = 0L;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getContractType() { return contractType; }
    public void setContractType(String contractType) { this.contractType = contractType; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    
    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
    
    public String getResponsibilities() { return responsibilities; }
    public void setResponsibilities(String responsibilities) { this.responsibilities = responsibilities; }
    
    public String getQualifications() { return qualifications; }
    public void setQualifications(String qualifications) { this.qualifications = qualifications; }
    
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    
    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }
    
    public String getApplicationEmail() { return applicationEmail; }
    public void setApplicationEmail(String applicationEmail) { this.applicationEmail = applicationEmail; }
    
    public String getApplicationUrl() { return applicationUrl; }
    public void setApplicationUrl(String applicationUrl) { this.applicationUrl = applicationUrl; }
    
    public String getApplicationInstructions() { return applicationInstructions; }
    public void setApplicationInstructions(String applicationInstructions) { this.applicationInstructions = applicationInstructions; }
    
    public String getRequiredDocuments() { return requiredDocuments; }
    public void setRequiredDocuments(String requiredDocuments) { this.requiredDocuments = requiredDocuments; }
    
    public Integer getNumberOfPositions() { return numberOfPositions; }
    public void setNumberOfPositions(Integer numberOfPositions) { this.numberOfPositions = numberOfPositions; }
    
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
    
    public Boolean getIsPublished() { return isPublished; }
    public void setIsPublished(Boolean isPublished) { this.isPublished = isPublished; }
    
    public JobOfferStatus getStatus() { return status; }
    public void setStatus(JobOfferStatus status) { this.status = status; }
    
    public Long getApplicationCount() { return applicationCount; }
    public void setApplicationCount(Long applicationCount) { this.applicationCount = applicationCount; }
    
    public Long getViewCount() { return viewCount; }
    public void setViewCount(Long viewCount) { this.viewCount = viewCount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
}
