package bf.gov.matm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRequest {
    
    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 255, message = "Le titre ne doit pas dépasser 255 caractères")
    private String title;
    
    @Size(max = 5000, message = "La description ne doit pas dépasser 5000 caractères")
    private String description;

    @Size(max = 255, message = "Le statut ne doit pas dépasser 255 caractères")
    private String statut;
    
    @NotNull(message = "La date de début est obligatoire")
    private LocalDateTime startDate;

    private String content;
    
    private LocalDateTime endDate;
    
    @Size(max = 500, message = "Le lieu ne doit pas dépasser 500 caractères")
    private String location;
    
    private String imageUrl;
    
    private String category;
    
    private Boolean isPublic = true;
    
    private Integer maxParticipants;
    
    private Boolean registrationRequired = false;

    // Getters and Setters
//    public String getTitle() { return title; }
//    public void setTitle(String title) { this.title = title; }
//
//    public String getDescription() { return description; }
//    public void setDescription(String description) { this.description = description; }
//
//    public LocalDateTime getStartDate() { return startDate; }
//    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
//
//    public LocalDateTime getEndDate() { return endDate; }
//    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
//
//    public String getLocation() { return location; }
//    public void setLocation(String location) { this.location = location; }
//
//    public String getImageUrl() { return imageUrl; }
//    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
//
//    public String getCategory() { return category; }
//    public void setCategory(String category) { this.category = category; }
//
//    public Boolean getIsPublic() { return isPublic; }
//    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
//
//    public Integer getMaxParticipants() { return maxParticipants; }
//    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }
//
//    public Boolean getRegistrationRequired() { return registrationRequired; }
//    public void setRegistrationRequired(Boolean registrationRequired) { this.registrationRequired = registrationRequired; }
//
//    public void setContent(String content)
//    {
//        this.content = content;
//    }
//    public String getContent()
//    {
//        return content;
//    }
}
