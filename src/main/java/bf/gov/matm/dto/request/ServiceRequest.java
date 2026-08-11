package bf.gov.matm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public class ServiceRequest {
    
    @NotBlank(message = "Le nom du service est obligatoire")
    @Size(max = 255, message = "Le nom ne doit pas dépasser 255 caractères")
    private String name;
    
    @Size(max = 5000, message = "La description ne doit pas dépasser 5000 caractères")
    private String description;
    
    @NotBlank(message = "La catégorie est obligatoire")
    private String category;
    
    private String subcategory;
    
    private String targetAudience; // PARTICULIERS, ENTREPRISES, BOTH
    
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
    
    private Boolean isOnline = false;
    
    private Boolean isActive = true;
    
    private Integer displayOrder;
    
    private String iconName;

    // Getters and Setters
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
    
    public String getIconName() { return iconName; }
    public void setIconName(String iconName) { this.iconName = iconName; }
}
