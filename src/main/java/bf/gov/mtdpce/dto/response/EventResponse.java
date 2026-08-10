package bf.gov.mtdpce.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventResponse {
    
    private Long id;
    private String title;
    private String description;
    private String content;
    private String statut;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private String imageUrl;
    private String category;
    private Boolean isPublic;
    private Integer maxParticipants;
    private Integer currentParticipants;
    private Boolean registrationRequired;
    private Boolean isRegistrationOpen;
    private String status; // UPCOMING, ONGOING, COMPLETED, CANCELLED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;

    // Getters and Setters
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
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
//    public Integer getCurrentParticipants() { return currentParticipants; }
//    public void setCurrentParticipants(Integer currentParticipants) { this.currentParticipants = currentParticipants; }
//
//    public Boolean getRegistrationRequired() { return registrationRequired; }
//    public void setRegistrationRequired(Boolean registrationRequired) { this.registrationRequired = registrationRequired; }
//
//    public Boolean getIsRegistrationOpen() { return isRegistrationOpen; }
//    public void setIsRegistrationOpen(Boolean isRegistrationOpen) { this.isRegistrationOpen = isRegistrationOpen; }
//
//    public String getStatus() { return status; }
//    public void setStatus(String status) { this.status = status; }
//
//    public LocalDateTime getCreatedAt() { return createdAt; }
//    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
//
//    public LocalDateTime getUpdatedAt() { return updatedAt; }
//    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
//
//    public String getCreatedBy() { return createdBy; }
//    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
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
