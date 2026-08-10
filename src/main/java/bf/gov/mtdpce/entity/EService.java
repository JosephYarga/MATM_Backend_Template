package bf.gov.mtdpce.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "e_services")
public class EService {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 255)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(length = 100)
    private String category;
    
    @Column(length = 100)
    private String subcategory;
    
    @Column(name = "target_audience", length = 255)
    private String targetAudience;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal cost;
    
    @Column(name = "cost_details", columnDefinition = "TEXT")
    private String costDetails;
    
    @Column(name = "processing_time", length = 100)
    private String processingTime;
    
    @Column(name = "access_conditions", columnDefinition = "TEXT")
    private String accessConditions;
    
    @Column(name = "required_documents", columnDefinition = "TEXT")
    private String requiredDocuments;
    
    @Column(name = "procedure_steps", columnDefinition = "TEXT")
    private String procedureSteps;
    
    @Column(name = "online_url", length = 500)
    private String onlineUrl;
    
    @Column(name = "physical_address", length = 500)
    private String physicalAddress;
    
    @Column(name = "contact_email", length = 255)
    private String contactEmail;
    
    @Column(name = "contact_phone", length = 50)
    private String contactPhone;
    
    @Column(name = "example_file_url", length = 500)
    private String exampleFileUrl;
    
    @Column(name = "icon_name", length = 50)
    private String iconName;
    
    @Column(name = "is_online")
    private Boolean isOnline = false;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "display_order")
    private Integer displayOrder = 0;
    
    @Column(name = "view_count")
    private Long viewCount = 0L;
    
    @Column(name = "request_count")
    private Long requestCount = 0L;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
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
    
    public String getRequiredDocuments() { return requiredDocuments; }
    public void setRequiredDocuments(String requiredDocuments) { this.requiredDocuments = requiredDocuments; }
    
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
    
    public String getIconName() { return iconName; }
    public void setIconName(String iconName) { this.iconName = iconName; }
    
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
