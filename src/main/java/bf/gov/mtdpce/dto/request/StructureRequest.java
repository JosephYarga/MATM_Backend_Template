package bf.gov.mtdpce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StructureRequest {
    
    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 255, message = "Le nom ne doit pas dépasser 255 caractères")
    private String name;
    
    @Size(max = 100, message = "L'acronyme ne doit pas dépasser 100 caractères")
    private String acronym;
    
    @Size(max = 5000, message = "La description ne doit pas dépasser 5000 caractères")
    private String description;
    
    @Size(max = 5000, message = "Les missions ne doivent pas dépasser 5000 caractères")
    private String missions;
    
    @NotBlank(message = "Le type de structure est obligatoire")
    private String structureType; // DIRECTION, SERVICE, AGENCE, ETABLISSEMENT
    
    private Long parentStructureId;
    
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
    
    private Boolean isActive = true;

    // Getters and Setters
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
}
