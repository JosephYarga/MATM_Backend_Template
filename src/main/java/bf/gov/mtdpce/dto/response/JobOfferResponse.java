package bf.gov.mtdpce.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class JobOfferResponse {
    
    private Long id;
    private String title;
    private String description;
    private String contractType;
    private String department;
    private String location;
    private LocalDate deadline;
    private List<String> requirements;
    private List<String> responsibilities;
    private String qualifications;
    private String experience;
    private String salary;
    private String applicationEmail;
    private String applicationUrl;
    private String applicationInstructions;
    private List<String> requiredDocuments;
    private Integer numberOfPositions;
    private String referenceNumber;
    private Boolean isPublished;
    private String status; // OPEN, CLOSED, FILLED
    private Long applicationCount;
    private Long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;

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
    
    public List<String> getRequirements() { return requirements; }
    public void setRequirements(List<String> requirements) { this.requirements = requirements; }
    
    public List<String> getResponsibilities() { return responsibilities; }
    public void setResponsibilities(List<String> responsibilities) { this.responsibilities = responsibilities; }
    
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
    
    public List<String> getRequiredDocuments() { return requiredDocuments; }
    public void setRequiredDocuments(List<String> requiredDocuments) { this.requiredDocuments = requiredDocuments; }
    
    public Integer getNumberOfPositions() { return numberOfPositions; }
    public void setNumberOfPositions(Integer numberOfPositions) { this.numberOfPositions = numberOfPositions; }
    
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
    
    public Boolean getIsPublished() { return isPublished; }
    public void setIsPublished(Boolean isPublished) { this.isPublished = isPublished; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Long getApplicationCount() { return applicationCount; }
    public void setApplicationCount(Long applicationCount) { this.applicationCount = applicationCount; }
    
    public Long getViewCount() { return viewCount; }
    public void setViewCount(Long viewCount) { this.viewCount = viewCount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}
