package bf.gov.mtdpce.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 255)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 255)
    private String statut;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;
    
    @Column(name = "end_date")
    private LocalDateTime endDate;
    
    @Column(length = 500)
    private String location;
    
    @Column(name = "image_url", length = 500)
    private String imageUrl;
    
    @Column(length = 100)
    private String category;
    
    @Column(name = "is_public")
    private Boolean isPublic = true;
    
    @Column(name = "max_participants")
    private Integer maxParticipants;
    
    @Column(name = "current_participants")
    private Integer currentParticipants = 0;
    
    @Column(name = "registration_required")
    private Boolean registrationRequired = false;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EventStatus status = EventStatus.UPCOMING;
    
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
//    public EventStatus getStatus() { return status; }
//    public void setStatus(EventStatus status) { this.status = status; }
//
//    public LocalDateTime getCreatedAt() { return createdAt; }
//    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
//
//    public LocalDateTime getUpdatedAt() { return updatedAt; }
//    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
//
//    public User getCreatedBy() { return createdBy; }
//    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
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
