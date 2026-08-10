package bf.gov.mtdpce.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "faqs")
public class FAQ {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String answer;
    
    @Column(length = 100)
    private String category;
    
    @Column(columnDefinition = "TEXT")
    private String tags;
    
    @Column(name = "display_order")
    private Integer displayOrder = 0;
    
    @Column(name = "is_published")
    private Boolean isPublished = true;
    
    @Column(name = "view_count")
    private Long viewCount = 0L;
    
    @Column(name = "helpful_count")
    private Long helpfulCount = 0L;
    
    @Column(name = "not_helpful_count")
    private Long notHelpfulCount = 0L;
    
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
    
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    
    public Boolean getIsPublished() { return isPublished; }
    public void setIsPublished(Boolean isPublished) { this.isPublished = isPublished; }
    
    public Long getViewCount() { return viewCount; }
    public void setViewCount(Long viewCount) { this.viewCount = viewCount; }
    
    public Long getHelpfulCount() { return helpfulCount; }
    public void setHelpfulCount(Long helpfulCount) { this.helpfulCount = helpfulCount; }
    
    public Long getNotHelpfulCount() { return notHelpfulCount; }
    public void setNotHelpfulCount(Long notHelpfulCount) { this.notHelpfulCount = notHelpfulCount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
