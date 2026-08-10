package bf.gov.mtdpce.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class StructureResponse {
    
    private Long id;
    private String name;
    private String acronym;
    private String description;
    private String missions;
    private String structureType;
    private Long parentStructureId;
    private String parentStructureName;
    private List<StructureResponse> childStructures;
    private String address;
    private String city;
    private String phone;
    private String fax;
    private String email;
    private String website;
    private String logoUrl;
    private String directorName;
    private String directorTitle;
    private String openingHours;
    private Double latitude;
    private Double longitude;
    private Integer displayOrder;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getAcronym() { return acronym; }
    public void setAcronym(String acronym) { this.acronym = acronym; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getMissions() { return missions; }
    public void setMissions(String missions) { this.missions = missions; }
    
    public String getStructureType() { return structureType; }
    public void setStructureType(String structureType) { this.structureType = structureType; }
    
    public Long getParentStructureId() { return parentStructureId; }
    public void setParentStructureId(Long parentStructureId) { this.parentStructureId = parentStructureId; }
    
    public String getParentStructureName() { return parentStructureName; }
    public void setParentStructureName(String parentStructureName) { this.parentStructureName = parentStructureName; }
    
    public List<StructureResponse> getChildStructures() { return childStructures; }
    public void setChildStructures(List<StructureResponse> childStructures) { this.childStructures = childStructures; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getFax() { return fax; }
    public void setFax(String fax) { this.fax = fax; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    
    public String getDirectorName() { return directorName; }
    public void setDirectorName(String directorName) { this.directorName = directorName; }
    
    public String getDirectorTitle() { return directorTitle; }
    public void setDirectorTitle(String directorTitle) { this.directorTitle = directorTitle; }
    
    public String getOpeningHours() { return openingHours; }
    public void setOpeningHours(String openingHours) { this.openingHours = openingHours; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
