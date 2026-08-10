package bf.gov.mtdpce.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ServiceResponse {
    
    private Long id;
    private String name;
    private String description;
    private String category;
    private String subcategory;
    private String targetAudience;
    private BigDecimal cost;
    private String costDetails;
    private String processingTime;
    private String accessConditions;
    private List<String> requiredDocuments;
    private String procedureSteps;
    private String onlineUrl;
    private String physicalAddress;
    private String contactEmail;
    private String contactPhone;
    private String exampleFileUrl;
    private Boolean isOnline;
    private Boolean isActive;
    private Integer displayOrder;
    private Long viewCount;
    private Long requestCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getSubcategory() { return subcategory; }
    public void setSubcategory(String subcategory) { this.subcategory = subcategory; }
    
    public String getTargetAudience() { return targetAudience; }
    public void setTargetAudience(String targetAudience) { this.targetAudience = targetAudience; }
    
    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }
    
    public String getCostDetails() { return costDetails; }
    public void setCostDetails(String costDetails) { this.costDetails = costDetails; }
    
    public String getProcessingTime() { return processingTime; }
    public void setProcessingTime(String processingTime) { this.processingTime = processingTime; }
    
    public String getAccessConditions() { return accessConditions; }
    public void setAccessConditions(String accessConditions) { this.accessConditions = accessConditions; }
    
    public List<String> getRequiredDocuments() { return requiredDocuments; }
    public void setRequiredDocuments(List<String> requiredDocuments) { this.requiredDocuments = requiredDocuments; }
    
    public String getProcedureSteps() { return procedureSteps; }
    public void setProcedureSteps(String procedureSteps) { this.procedureSteps = procedureSteps; }
    
    public String getOnlineUrl() { return onlineUrl; }
    public void setOnlineUrl(String onlineUrl) { this.onlineUrl = onlineUrl; }
    
    public String getPhysicalAddress() { return physicalAddress; }
    public void setPhysicalAddress(String physicalAddress) { this.physicalAddress = physicalAddress; }
    
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    
    public String getExampleFileUrl() { return exampleFileUrl; }
    public void setExampleFileUrl(String exampleFileUrl) { this.exampleFileUrl = exampleFileUrl; }
    
    public Boolean getIsOnline() { return isOnline; }
    public void setIsOnline(Boolean isOnline) { this.isOnline = isOnline; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    
    public Long getViewCount() { return viewCount; }
    public void setViewCount(Long viewCount) { this.viewCount = viewCount; }
    
    public Long getRequestCount() { return requestCount; }
    public void setRequestCount(Long requestCount) { this.requestCount = requestCount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
